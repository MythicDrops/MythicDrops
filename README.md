# MythicDrops

MythicDrops is an open source Bukkit plugin that brings an RPG-like system of drops to Minecraft. Items can
have unique names, lore, and enchantments added randomly to them through a tier system.

[![Maven Central](https://img.shields.io/maven-central/v/io.pixeloutlaw.mythicdrops/mythicdrops?style=flat-square)](https://repo1.maven.org/maven2/io/pixeloutlaw/mythicdrops/mythicdrops/)
[![javadoc](https://javadoc.io/badge2/io.pixeloutlaw.mythicdrops/mythicdrops/javadoc.svg?style=flat-square)](https://javadoc.io/doc/io.pixeloutlaw.mythicdrops/mythicdrops)

## How To Get It (Server Runners)

https://www.spigotmc.org/resources/mythicdrops.6114/

## How to Get It (Developers)

### Gradle

#### Maven Central

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compileOnly "io.pixeloutlaw.mythicdrops:mythicdrops:x.y.z"
}
```

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.pixeloutlaw.mythicdrops:mythicdrops:x.y.z")
}
```

### Maven

#### Maven Central

```xml
<dependencies>
    <!-- other dependencies... -->
    <dependency>
        <groupId>io.pixeloutlaw.mythicdrops</groupId>
        <artifactId>mythicdrops</artifactId>
        <version>x.y.z</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
