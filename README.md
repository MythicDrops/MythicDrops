# MythicDrops

MythicDrops is an open source Bukkit plugin that brings an RPG-like system of drops to Minecraft. Items can
have unique names, lore, and enchantments added randomly to them through a tier system.

[![Bintray](https://img.shields.io/bintray/v/pixeloutlaw/mythicdrops/mythicdrops?style=flat-square)](https://bintray.com/pixeloutlaw/mythicdrops/mythicdrops)
[![javadoc](https://javadoc.io/badge2/io.pixeloutlaw.mythicdrops/mythicdrops/javadoc.svg?style=flat-square)](https://javadoc.io/doc/io.pixeloutlaw.mythicdrops/mythicdrops)

## How To Get It (Server Runners)

https://www.spigotmc.org/resources/mythicdrops.6114/

## How to Get It (Developers)

### Gradle

#### JCenter

```groovy
repositories {
    jcenter()
}

dependencies {
    compileOnly "io.pixeloutlaw.mythicdrops:mythicdrops:x.y.z"
}
```

```kotlin
repositories {
    jcenter()
}

dependencies {
    compileOnly("io.pixeloutlaw.mythicdrops:mythicdrops:x.y.z")
}
```

#### Bintray

```groovy
repositories {
    maven {
        url "https://dl.bintray.com/pixeloutlaw/mythicdrops"
    }
}

dependencies {
    compileOnly "io.pixeloutlaw.mythicdrops:mythicdrops:x.y.z"
}
```

```kotlin
repositories {
    maven {
        url = uri("https://dl.bintray.com/pixeloutlaw/mythicdrops")
    }
}

dependencies {
    compileOnly("io.pixeloutlaw.mythicdrops:mythicdrops:x.y.z")
}
```

### Maven

#### JCenter

Add JCenter to your POM:

```xml
<repositories>
    <!-- other repositories... -->
    <repository>
        <id>central</id>
        <url>https://jcenter.bintray.com</url>
    </repository>
</repositories>

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

#### Bintray

Add the Bintray repository to your POM:

```xml
<repositories>
    <!-- other repositories... -->
    <repository>
        <id>mythicdrops-bintray</id>
        <url>https://dl.bintray.com/pixeloutlaw/mythicdrops</url>
    </repository>
</repositories>

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
