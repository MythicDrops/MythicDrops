package org.bukkit.plugin.java.annotation.dependency;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a plugin dependency. <br>
 * The plugin's <b>name</b> attribute is required in order to load the dependency.<br>
 * If any plugin listed is not found the plugin will fail to load. <br>
 * If multiple plugins list each other as a dependency, so that there are no plugins with an
 * unloadable dependency, all plugins will fail to load.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DependsOn.class)
public @interface Dependency {
  /** A plugin that is required to be present in order for this plugin to load. */
  String value();
}
