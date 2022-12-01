/*
 * Copyright (C) 2017 Stanley Shyiko
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.pixeloutlaw.minecraft.spigot.klob

import io.pixeloutlaw.minecraft.spigot.klob.internal.visit
import java.nio.file.Path
import java.util.EnumSet

internal interface Glob {
    companion object {
        val EMPTY_IP_SET: EnumSet<IterationOption> = EnumSet.noneOf(IterationOption::class.java)
        fun from(vararg pattern: String): Glob {
            return object : Glob {
                override fun iterate(path: Path, iterationOptions: Collection<IterationOption>): Iterator<Path> {
                    return visit(
                        path,
                        if (iterationOptions.isEmpty()) {
                            EMPTY_IP_SET
                        } else {
                            EnumSet.copyOf(iterationOptions)
                        },
                        pattern.toList()
                    ).iterator()
                }

                override fun iterate(path: Path) = iterate(path, emptySet())
            }
        }
    }

    enum class IterationOption {
        /**
         * Skip hidden files/directories.
         */
        SKIP_HIDDEN,

        /**
         * By default children are included, e.g. "src" matches not just "src" but "src/path/to/file".
         * Use this option to change that.
         */
        SKIP_CHILDREN,

        /**
         * Iterate over directories instead of files.
         */
        DIRECTORY
    }

    fun iterate(path: Path, iterationOptions: Collection<IterationOption>): Iterator<Path>
    fun iterate(path: Path): Iterator<Path>
}
