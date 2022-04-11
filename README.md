# MythicDrops

MythicDrops is an open source Spigot plugin that brings an RPG-like system of drops to Minecraft. Items can
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
    compileOnly "io.pixeloutlaw.mythicdrops:mythicdrops-api:x.y.z"
}
```

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.pixeloutlaw.mythicdrops:mythicdrops-api:x.y.z")
}
```

### Maven

#### Maven Central

```xml
<dependencies>
    <!-- other dependencies... -->
    <dependency>
        <groupId>io.pixeloutlaw.mythicdrops</groupId>
        <artifactId>mythicdrops-api</artifactId>
        <version>x.y.z</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Sponsors

### Richard Harrah Sponsors

<p align="center">
  <a href="https://cdn.jsdelivr.net/gh/ToppleTheNun/static/sponsors.svg">
    <img src='https://cdn.jsdelivr.net/gh/ToppleTheNun/static/sponsors.svg'/>
  </a>
</p>

## License

[MIT](./LICENSE) License © 2013 [Richard Harrah](https://github.com/ToppleTheNun)
