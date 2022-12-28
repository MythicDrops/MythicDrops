package io.pixeloutlaw.minecraft.spigot

import java.io.Serializable

internal interface ResettableLazy<out T> : Lazy<T> {
    fun reset(): Unit
}

internal fun <T> resettableLazy(initializer: () -> T): ResettableLazy<T> = SynchronizedResettableLazy(initializer)

internal object UNINITIALIZED_VALUE
private class SynchronizedResettableLazy<out T>(initializer: () -> T, lock: Any? = null) :
    ResettableLazy<T>,
    Serializable {
    companion object {
        const val serialVersionUID: Long = 1
    }

    private var initializer: (() -> T)? = initializer

    @Volatile
    private var _value: Any? = UNINITIALIZED_VALUE

    // final field is required to enable safe publication of constructed instance
    private val lock = lock ?: this

    override val value: T
        get() {
            val _v1 = _value
            if (_v1 !== UNINITIALIZED_VALUE) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_VALUE) {
                    @Suppress("UNCHECKED_CAST")
                    (_v2 as T)
                } else {
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun reset() {
        _value = UNINITIALIZED_VALUE
    }

    override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."

    private fun writeReplace(): Any = InitializedLazyImpl(value)
}

private class InitializedLazyImpl<out T>(override val value: T) : ResettableLazy<T>, Serializable {
    companion object {
        const val serialVersionUID: Long = 1
    }

    override fun isInitialized(): Boolean = true

    override fun toString(): String = value.toString()

    override fun reset() {}
}
