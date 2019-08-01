package org.bukkit.plugin.java.annotation.dependency;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 *
 * <p>Represents the plugins a plugin depends on in order to be loaded <br>
 * This specific annotation should not be used by people who do not know how repeating annotations
 * work.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DependsOn {
  Dependency[] value() default {};
}
