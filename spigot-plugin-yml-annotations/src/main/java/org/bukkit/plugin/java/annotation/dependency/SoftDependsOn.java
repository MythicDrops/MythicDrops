package org.bukkit.plugin.java.annotation.dependency;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 *
 * <p>Represents the plugins this plugin should try to load before this plugin will attempt to load.
 * A plugin will still load if a soft dependency is not present. <br>
 * This specific annotation should not be used by people who do not know how repeating annotations
 * work.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SoftDependsOn {
  SoftDependency[] value() default {};
}
