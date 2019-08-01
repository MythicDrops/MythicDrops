package org.bukkit.plugin.java.annotation.plugin.author;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 *
 * <p>Represents the author(s) of the plugin. Translates to {@code author} in plugin.yml if a single
 * author, otherwise {@code authors}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Authors.class)
public @interface Author {
  /** The name of the person who developed this plugin. */
  String value();
}
