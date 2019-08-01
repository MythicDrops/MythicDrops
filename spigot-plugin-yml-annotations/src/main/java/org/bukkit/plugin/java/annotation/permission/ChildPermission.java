package org.bukkit.plugin.java.annotation.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Defines a child permission for a {@link Permission} */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChildPermission {
  /**
   * If true, this child node will inherit the parent {@link Permission}'s permission. If false,
   * this child node inherits the inverse of the parent permission.
   */
  boolean inherit() default true;

  /** The name of the child permission. */
  String name();
}
