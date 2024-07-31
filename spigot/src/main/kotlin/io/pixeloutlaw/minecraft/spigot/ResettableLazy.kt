package io.pixeloutlaw.minecraft.spigot

internal fun <T> resettableLazy(value: () -> T) = ResettableLazy(value)

internal class ResettableLazy<T>(
    private val initializer: () -> T
) : Lazy<T> {
    private var cached: T? = null
    override val value: T
        get() {
            return cached ?: return initializer().also { cached = it }
        }

    override fun isInitialized(): Boolean = cached != null

    fun reset() {
        cached = null
    }
}
