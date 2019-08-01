package org.bukkit.plugin.java.annotation.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.permissions.PermissionDefault;

/** Defines a plugin permission */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Permissions.class)
public @interface Permission {
  /** This permission's name. */
  String name();

  /** This permission's description. */
  String desc() default "";

  /** This permission's default {@link PermissionDefault} */
  PermissionDefault defaultValue() default PermissionDefault.OP;

  /** This permission's child nodes ( {@link ChildPermission} ) */
  ChildPermission[] children() default {};
}
