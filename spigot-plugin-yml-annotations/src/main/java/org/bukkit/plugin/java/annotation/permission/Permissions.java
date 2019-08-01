package org.bukkit.plugin.java.annotation.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 *
 * <p>Represents a list of this plugin's registered name. <br>
 * This specific annotation should not be used by people who do not know how repeating annotations
 * work.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Permissions {
  Permission[] value() default {};
}
