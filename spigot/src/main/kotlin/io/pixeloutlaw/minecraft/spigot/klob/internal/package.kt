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

import io.pixeloutlaw.minecraft.spigot.klob.Glob.IterationOption
import io.pixeloutlaw.minecraft.spigot.klob.internal.Glob.Companion
import java.io.File
import java.io.FileFilter
import java.nio.file.Path
import java.util.EnumSet

internal fun FileFilter.and(fileFilter: FileFilter) =
    FileFilter { file -> this@and.accept(file) && fileFilter.accept(file) }

/**
 * On Windows: "C:\io" -> "/C:/io", does nothing everywhere else.
 */
internal fun slash(path: String) = path.replace('\\', '/')
    .let { if (!it.startsWith("/") && it.contains(":/")) "/$it" else it }

internal fun fromSlash(path: String) = path.replace('/', File.separatorChar)
    .let { if (it.contains(":/")) it.removePrefix("/") else it }

internal fun visit(dir: File, filter: FileFilter, directoryModeFilter: FileFilter?): Sequence<Path> {
    val stack = ArrayDeque<File>().apply { add(dir) }
    return generateSequence {
        while (true) {
            val file = stack.removeLastOrNull() ?: break
            if (file.isFile && directoryModeFilter == null) {
                return@generateSequence file.toPath()
            }
            if (file.isDirectory) {
                val fileList = file.listFiles(filter)
                fileList.sortDescending()
                if (fileList != null) {
                    stack.addAll(fileList)
                }
                if (directoryModeFilter != null && directoryModeFilter.accept(file)) {
                    return@generateSequence file.toPath()
                }
            }
        }
        return@generateSequence null
    }
}

internal fun visit(path: Path, option: EnumSet<IterationOption>, patterns: List<String>): Sequence<Path> {
    val includeChildren = !option.contains(IterationOption.SKIP_CHILDREN)
    val directoryMode = option.contains(IterationOption.DIRECTORY)
    if (includeChildren && directoryMode) {
        throw UnsupportedOperationException(
            "Glob.IterationOption.DIRECTORY must be used together with Glob.IterationOption.SKIP_CHILDREN " +
                "(please create a ticket at https://github.com/shyiko/klob/issue if it doesn't fit your needs)"
        )
    }
    val baseDir = path.toString()
    val filter = GlobFileFilter(
        baseDir,
        patterns,
        includeChildren = includeChildren
    ).let {
        if (option.contains(IterationOption.SKIP_HIDDEN)) {
            it.and(HiddenFileFilter(reverse = true))
        } else {
            it
        }
    }
    val directoryModeFilter = if (option.contains(IterationOption.DIRECTORY)) {
        GlobFileFilter(
            baseDir,
            patterns,
            includeChildren = includeChildren,
            forceExactMatch = true
        )
    } else {
        null
    }
    return patterns
        .asSequence()
        .map { Companion.prefix(slash(it)) }
        .distinct()
        .map { (if (it.startsWith("/")) File(fromSlash(it)) else File(baseDir, fromSlash(it))).canonicalPath }
        // remove overlapping paths (e.g. /a & /a/b -> /a)
        .sorted()
        .fold(mutableListOf<String>()) { r, v ->
            if (r.isEmpty() || !v.startsWith(r.last())) {
                r.add(v)
            }
            r
        }
        .map { visit(File(it), filter, directoryModeFilter) }
        .asSequence()
        .flatten()
}
