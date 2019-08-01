package org.bukkit.plugin.java.annotation.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Defines a plugin command */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Commands.class)
public @interface Command {
  /** This command's name. */
  String name();

  /** This command's description. */
  String desc() default "";

  /** This command's aliases. */
  String[] aliases() default {};

  /** This command's permission node. */
  String permission() default "";

  /** This command's permission-check-fail message. */
  String permissionMessage() default "";

  /** This command's usage message. */
  String usage() default "";
}
