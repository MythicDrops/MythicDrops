package io.pixeloutlaw.minecraft.spigot.exceptions

// Wrapper around RuntimeException to make detekt happy
class SomethingWentWrongException(exception: Throwable) : RuntimeException(exception)
