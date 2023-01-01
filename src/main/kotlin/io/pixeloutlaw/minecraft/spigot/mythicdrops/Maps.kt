package io.pixeloutlaw.minecraft.spigot.mythicdrops

/**
 * Variant of [Map.getOrDefault] that also checks the type of the returned value.
 */
inline fun <Key, Value, reified DesiredValueType> Map<Key, Value>.getOrDefaultAsDefaultValueType(
    key: Key,
    defaultValue: DesiredValueType
): DesiredValueType {
    val fromMap = getOrDefault(key, defaultValue)
    if (fromMap is DesiredValueType) {
        return fromMap
    }
    return defaultValue
}
