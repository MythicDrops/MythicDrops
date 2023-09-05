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
package io.pixeloutlaw.minecraft.spigot.klob.internal

import java.io.File
import java.io.FileFilter

internal class GlobFileFilter(
    baseDir: String,
    patterns: List<String>,
    includeChildren: Boolean = true,
    val forceExactMatch: Boolean = false
) : FileFilter {
    private val glob: Collection<Glob> = patterns.map {
        Glob(
            slash(baseDir),
            slash(it),
            includeChildren = includeChildren
        )
    }

    /**
     * @return true if at least one glob explicitly includes file (any matching file excluded by a previous pattern
     * will become included again), false otherwise
     */
    override fun accept(file: File): Boolean {
        return glob.fold(false) { r, g ->
            val absolutePath = slash(file.absolutePath)
            if (g.pattern.startsWith("!")) {
                return@fold g.matches(absolutePath) && r
            }
            return@fold g.matches(absolutePath, forceExactMatch || !file.isDirectory) || r
        }
    }
}
