package io.pixeloutlaw.minecraft.spigot.mythicdrops

/**
 * Attempts to convert the given [String] into a value from the specified enum or `null` if it can't.
 *
 * @param str String representation of enum value
 * @return enum value or null
 */
internal inline fun <reified T : Enum<T>> enumValueOrNull(str: String?): T? {
    return if (str == null) {
        null
    } else {
        try {
            java.lang.Enum.valueOf(T::class.java, str)
        } catch (ex: IllegalArgumentException) {
            // handles the case where valueOf throws IAE due to not being an enum value
            null
        }
    }
}
