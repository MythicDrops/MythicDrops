package org.bukkit.plugin.java.annotation.plugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * This annotation specifies the api version of the plugin. <br>
 * Defaults to {@link ApiVersion.Target#DEFAULT}. <br>
 * Pre-1.13 plugins do not need to use this annotation.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiVersion {

  Target value() default Target.DEFAULT;

  /**
   * Specifies the target api-version for this plugin.
   *
   * <p>All pre-1.13 plugins must use {@link #DEFAULT}.
   */
  public static enum Target {
    /** This target version specifies that the plugin was made for pre-1.13 Spigot versions. */
    DEFAULT(null),

    /** This target version specifies that the plugin was made with 1.13+ versions in mind. */
    v1_13("1.13", DEFAULT),

    /** This target version specifies that the plugin was made with 1.14+ versions in mind. */
    v1_14("1.14", DEFAULT);

    private final String version;
    private final Collection<Target> conflictsWith = Sets.newLinkedHashSet();

    Target(String version, Target... conflictsWith) {
      this.version = version;
      this.conflictsWith.addAll(Lists.newArrayList(conflictsWith));
    }

    public String getVersion() {
      return version;
    }

    public boolean conflictsWith(Target target) {
      return this.conflictsWith.contains(target);
    }
  }
}
