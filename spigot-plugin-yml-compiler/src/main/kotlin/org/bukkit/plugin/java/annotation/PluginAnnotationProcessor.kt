package org.bukkit.plugin.java.annotation

import com.google.auto.service.AutoService
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import org.bukkit.command.CommandExecutor
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.command.Commands
import org.bukkit.plugin.java.annotation.dependency.Dependency
import org.bukkit.plugin.java.annotation.dependency.LoadBefore
import org.bukkit.plugin.java.annotation.dependency.SoftDependency
import org.bukkit.plugin.java.annotation.permission.Permission
import org.bukkit.plugin.java.annotation.permission.Permissions
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.LoadOrder
import org.bukkit.plugin.java.annotation.plugin.LogPrefix
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.Website
import org.bukkit.plugin.java.annotation.plugin.author.Author
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Tag
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter
import javax.tools.Diagnostic
import javax.tools.StandardLocation

@AutoService(Processor::class)
@SupportedAnnotationTypes("org.bukkit.plugin.java.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@Suppress("TooManyFunctions")
class PluginAnnotationProcessor : AbstractProcessor() {
    private var hasMainBeenFound = false

    @Suppress("ComplexMethod", "LongMethod", "ReturnCount")
    override fun process(annots: Set<TypeElement>, rEnv: RoundEnvironment): Boolean {
        val mainPluginElement: Element?
        hasMainBeenFound = false

        val elements = rEnv.getElementsAnnotatedWith(Plugin::class.java)
        if (elements.size > 1) {
            raiseError("Found more than one plugin main class")
            return false
        }

        if (elements.isEmpty()) { // don't raise error because we don't know which run this is
            return false
        }
        if (hasMainBeenFound) {
            raiseError("The plugin class has already been located, aborting!")
            return false
        }
        mainPluginElement = elements.iterator().next()
        hasMainBeenFound = true

        val mainPluginType: TypeElement
        if (mainPluginElement is TypeElement) {
            mainPluginType = mainPluginElement
        } else {
            raiseError("Main plugin class is not a class", mainPluginElement)
            return false
        }

        if (mainPluginType.enclosingElement !is PackageElement) {
            raiseError("Main plugin class is not a top-level class", mainPluginType)
            return false
        }

        if (mainPluginType.modifiers.contains(Modifier.STATIC)) {
            raiseError("Main plugin class cannot be static nested", mainPluginType)
            return false
        }

        if (!processingEnv.typeUtils.isSubtype(mainPluginType.asType(), fromClass(JavaPlugin::class.java))) {
            raiseError("Main plugin class is not an subclass of JavaPlugin!", mainPluginType)
        }

        if (mainPluginType.modifiers.contains(Modifier.ABSTRACT)) {
            raiseError("Main plugin class cannot be abstract", mainPluginType)
            return false
        }

        // check for no-args constructor
        checkForNoArgsConstructor(mainPluginType)

        val yml = Maps.newLinkedHashMap<String, Any>() // linked so we can maintain the same output into file for sanity

        // populate mainName
        val mainName = mainPluginType.qualifiedName.toString()
        yml["main"] = mainName // always override this so we make sure the main class name is correct

        // populate plugin name
        processAndPut(
            yml,
            "name",
            mainPluginType,
            mainName.substring(mainName.lastIndexOf('.') + 1),
            Plugin::class.java,
            String::class.java,
            "name"
        )

        // populate version
        processAndPut(
            yml,
            "version",
            mainPluginType,
            Plugin.DEFAULT_VERSION,
            Plugin::class.java,
            String::class.java,
            "version"
        )

        // populate plugin description
        processAndPut(yml, "description", mainPluginType, null, Description::class.java, String::class.java)

        // populate plugin load order
        processAndPut(yml, "load", mainPluginType, null, LoadOrder::class.java, String::class.java)

        // authors
        val authors = mainPluginType.getAnnotationsByType(Author::class.java)
        val authorMap = Lists.newArrayList<String>()
        for (auth in authors) {
            authorMap.add(auth.value)
        }
        if (authorMap.size > 1) {
            yml["authors"] = authorMap
        } else if (authorMap.size == 1) {
            yml["author"] = authorMap.iterator().next()
        }

        // website
        processAndPut(yml, "website", mainPluginType, null, Website::class.java, String::class.java)

        // prefix
        processAndPut(yml, "prefix", mainPluginType, null, LogPrefix::class.java, String::class.java)

        // dependencies
        val dependencies = mainPluginType.getAnnotationsByType(Dependency::class.java)
        val hardDependencies = Lists.newArrayList<String>()
        for (dep in dependencies) {
            hardDependencies.add(dep.value)
        }
        if (hardDependencies.isNotEmpty()) {
            yml["depend"] = hardDependencies
        }

        // soft-dependencies
        val softDependencies = mainPluginType.getAnnotationsByType(SoftDependency::class.java)
        val softDepArr = arrayOfNulls<String>(softDependencies.size)
        for (i in softDependencies.indices) {
            softDepArr[i] = softDependencies[i].value
        }
        if (softDepArr.isNotEmpty()) {
            yml["softdepend"] = softDepArr
        }

        // load-before
        val loadBefore = mainPluginType.getAnnotationsByType(LoadBefore::class.java)
        val loadBeforeArr = arrayOfNulls<String>(loadBefore.size)
        for (i in loadBefore.indices) {
            loadBeforeArr[i] = loadBefore[i].value
        }
        if (loadBeforeArr.isNotEmpty()) {
            yml["loadbefore"] = loadBeforeArr
        }

        // commands
        // Begin processing external command annotations
        var commandMap: MutableMap<String, Map<String, Any>> = Maps.newLinkedHashMap()
        val validCommandExecutors =
            processExternalCommands(rEnv.getElementsAnnotatedWith(Commands::class.java), mainPluginType, commandMap)
        if (!validCommandExecutors) {
            // #processExternalCommand already raised the errors
            return false
        }

        val commands = mainPluginType.getAnnotation(Commands::class.java)

        // Check main class for any command annotations
        if (commands != null) {
            val merged = Maps.newLinkedHashMap<String, Map<String, Any>>()
            merged.putAll(commandMap)
            merged.putAll(this.processCommands(commands))
            commandMap = merged
        }

        yml["commands"] = commandMap

        // Permissions
        var permissionMetadata: MutableMap<String, Map<String, Any>> = Maps.newLinkedHashMap()

        val permissionAnnotations = rEnv.getElementsAnnotatedWith(Command::class.java)
        if (permissionAnnotations.size > 0) {
            for (element in permissionAnnotations) {
                if (element == mainPluginElement) {
                    continue
                }
                if (element.getAnnotation(Permission::class.java) != null) {
                    val permissionAnnotation = element.getAnnotation(Permission::class.java)
                    permissionMetadata[permissionAnnotation.name] = this.processPermission(permissionAnnotation)
                }
            }
        }

        val permissions = mainPluginType.getAnnotation(Permissions::class.java)
        if (permissions != null) {
            val joined = Maps.newLinkedHashMap<String, Map<String, Any>>()
            joined.putAll(permissionMetadata)
            joined.putAll(this.processPermissions(permissions))
            permissionMetadata = joined
        }

        // Process Permissions on command executors
        val validPermissions = processExternalPermissions(
            rEnv.getElementsAnnotatedWith(Permissions::class.java),
            mainPluginType,
            permissionMetadata
        )
        if (!validPermissions) {
            return false
        }
        yml["permissions"] = permissionMetadata

        // api-version
        if (mainPluginType.getAnnotation(ApiVersion::class.java) != null) {
            val apiVersion = mainPluginType.getAnnotation(ApiVersion::class.java)
            if (apiVersion.value != ApiVersion.Target.DEFAULT) {
                yml["api-version"] = apiVersion.value.version
            }
        }

        try {
            val yaml = Yaml()
            val file = this.processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml")
            file.openWriter().use { w ->
                w.append("# Auto-generated plugin.yml, generated at ")
                    .append(LocalDateTime.now().format(dFormat))
                    .append(" by ")
                    .append(this.javaClass.name)
                    .append("\n\n")
                // have to format the yaml explicitly because otherwise it dumps child nodes as maps within braces.
                val raw = yaml.dumpAs(yml, Tag.MAP, DumperOptions.FlowStyle.BLOCK)
                w.write(raw)
                w.flush()
                w.close()
            }
            // try with resources will close the Writer since it implements Closeable
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return true
    }

    @Suppress("ReturnCount")
    private fun processExternalPermissions(
        commandExecutors: Set<Element>,
        mainPluginType: TypeElement,
        yml: MutableMap<String, Map<String, Any>>
    ): Boolean {
        for (element in commandExecutors) {
            // Check to see if someone annotated a non-class with this
            if (element !is TypeElement) {
                this.raiseError("Specified Command Executor class is not a class.")
                return false
            }

            if (element == mainPluginType) {
                continue
            }

            // Check to see if annotated class is actuall a command executor
            val mirror = this.processingEnv.elementUtils.getTypeElement(CommandExecutor::class.java.name).asType()
            if (!this.processingEnv.typeUtils.isAssignable(element.asType(), mirror)) {
                this.raiseError("Specified Command Executor class is not assignable from CommandExecutor ")
                return false
            }

            val newMap = Maps.newLinkedHashMap<String, Map<String, Any>>()
            val annotation = element.getAnnotation(Permissions::class.java)
            if (annotation != null && annotation.value.isNotEmpty()) {
                newMap.putAll(processPermissions(annotation))
            }
            yml.putAll(newMap)
        }
        return true
    }

    private fun checkForNoArgsConstructor(mainPluginType: TypeElement) {
        for (constructor in ElementFilter.constructorsIn(mainPluginType.enclosedElements)) {
            if (constructor.parameters.isEmpty()) {
                return
            }
        }
        raiseError("Main plugin class must have a no argument constructor.", mainPluginType)
    }

    private fun raiseError(message: String) {
        this.processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
    }

    private fun raiseError(message: String, element: Element?) {
        this.processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message, element)
    }

    private fun fromClass(clazz: Class<*>): TypeMirror {
        return processingEnv.elementUtils.getTypeElement(clazz.name).asType()
    }

    @Suppress("LongParameterList")
    private fun <A : Annotation, R> processAndPut(
        map: MutableMap<String, Any?>,
        name: String,
        el: Element,
        defaultVal: R?,
        annotationType: Class<A>,
        returnType: Class<R>,
        methodName: String = "value"
    ): R? {
        val result = process(el, defaultVal, annotationType, returnType, methodName)
        if (result != null) {
            map[name] = result
        }
        return result
    }

    private fun <A : Annotation, R> process(
        el: Element,
        defaultVal: R?,
        annotationType: Class<A>,
        returnType: Class<R>,
        methodName: String
    ): R? {
        val result: R?
        val ann = el.getAnnotation(annotationType)
        result = if (ann == null) {
            defaultVal
        } else {
            try {
                val value = annotationType.getMethod(methodName)
                val res = value.invoke(ann)
                (if (returnType == String::class.java) res.toString() else returnType.cast(res)) as R?
            } catch (e: Exception) {
                throw RuntimeException(e) // shouldn't happen in theory (blame Choco if it does)
            }
        }
        return result
    }

    @Suppress("ReturnCount")
    private fun processExternalCommands(
        commandExecutors: Set<Element>,
        mainPluginType: TypeElement,
        commandMetadata: MutableMap<String, Map<String, Any>>
    ): Boolean {
        for (element in commandExecutors) {
            // Check to see if someone annotated a non-class with this
            if (element !is TypeElement) {
                this.raiseError("Specified Command Executor class is not a class.")
                return false
            }

            if (element == mainPluginType) {
                continue
            }

            // Check to see if annotated class is actuall a command executor
            val mirror = this.processingEnv.elementUtils.getTypeElement(CommandExecutor::class.java.name).asType()
            if (!this.processingEnv.typeUtils.isAssignable(element.asType(), mirror)) {
                this.raiseError("Specified Command Executor class is not assignable from CommandExecutor ")
                return false
            }

            val annotation = element.getAnnotation(Commands::class.java)
            if (annotation != null && annotation.value.isNotEmpty()) {
                commandMetadata.putAll(this.processCommands(annotation))
            }
        }
        return true
    }

    /**
     * Processes a set of commands.
     *
     * @param commands The annotation.
     *
     * @return The generated command metadata.
     */
    private fun processCommands(commands: Commands): Map<String, Map<String, Any>> {
        val commandList = Maps.newLinkedHashMap<String, Map<String, Any>>()
        for (command in commands.value) {
            commandList[command.name] = this.processCommand(command)
        }
        return commandList
    }

    /**
     * Processes a single command.
     *
     * @param commandAnnotation The annotation.
     *
     * @return The generated command metadata.
     */
    private fun processCommand(commandAnnotation: Command): Map<String, Any> {
        val command = Maps.newLinkedHashMap<String, Any>()

        if (commandAnnotation.aliases.size == 1) {
            command["aliases"] = commandAnnotation.aliases[0]
        } else if (commandAnnotation.aliases.size > 1) {
            command["aliases"] = commandAnnotation.aliases
        }

        if (commandAnnotation.desc.isNotEmpty()) {
            command["description"] = commandAnnotation.desc
        }
        if (commandAnnotation.permission.isNotEmpty()) {
            command["permission"] = commandAnnotation.permission
        }
        if (commandAnnotation.permissionMessage.isNotEmpty()) {
            command["permission-message"] = commandAnnotation.permissionMessage
        }
        if (commandAnnotation.usage.isNotEmpty()) {
            command["usage"] = commandAnnotation.usage
        }

        return command
    }

    /**
     * Processes a command.
     *
     * @param permissionAnnotation The annotation.
     *
     * @return The generated permission metadata.
     */
    private fun processPermission(permissionAnnotation: Permission): Map<String, Any> {
        val permission = Maps.newLinkedHashMap<String, Any>()

        if ("" != permissionAnnotation.desc) {
            permission["description"] = permissionAnnotation.desc
        }
        if (PermissionDefault.OP != permissionAnnotation.defaultValue) {
            permission["default"] = permissionAnnotation.defaultValue.toString().toLowerCase()
        }

        if (permissionAnnotation.children.isNotEmpty()) {
            val childrenList = Maps.newLinkedHashMap<String, Boolean>() // maintain order
            for (childPermission in permissionAnnotation.children) {
                childrenList[childPermission.name] = childPermission.inherit
            }
            permission["children"] = childrenList
        }

        return permission
    }

    /**
     * Processes a set of permissions.
     *
     * @param permissions The annotation.
     *
     * @return The generated permission metadata.
     */
    private fun processPermissions(permissions: Permissions): Map<String, Map<String, Any>> {
        val permissionList = Maps.newLinkedHashMap<String, Map<String, Any>>()
        for (permission in permissions.value) {
            permissionList[permission.name] = this.processPermission(permission)
        }
        return permissionList
    }

    companion object {
        private val dFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
    }
}
