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
import java.util.BitSet

/**
 * Glob, modeled after [gitignore](https://git-scm.com/docs/gitignore).
 *
 * Settings `restrictToBaseDir` and `includeChildren` to true should yield gitignore-mostly-compatible pattern
 * (leading "\#", "\!", trailing "\ " were "left out" + there is no distinction between files & directories).
 */
internal data class Glob(
    val baseDir: String,
    val pattern: String,
    val restrictToBaseDir: Boolean = false,
    val includeChildren: Boolean = false
) {

    companion object {

        fun isExclusionPattern(pattern: String) = pattern.startsWith("!")

        /**
         * @return everything before the first segment containing * (or an empty string if there is no such segment)
         */
        fun prefix(pattern: String) = pattern.removePrefix("!").removeSuffix("/").let {
            val split = it.split("/")
            val wildcardIndex = split.indexOfFirst { str -> str.contains("*") }.let { idx ->
                if (idx == -1 && split.size > 1) split.size - 1 else idx
            }
            if (wildcardIndex == -1) "" else split.subList(0, wildcardIndex).joinToString("/")
        }
    }

    private data class State(val regex: Regex, val optional: Boolean) {
        fun matches(input: CharSequence) = regex.matches(input)
    }

    private val state: List<State>
    private val negate: Boolean = isExclusionPattern(pattern)

    init {
        val prefix = prefix(pattern)
        val pattern = pattern
            .removePrefix("!")
            // remove trailing slash as we don't distinguish between files & directories
            // (even though gitignore does)
            .removeSuffix("/")
            // *.js -> **/*.js
            // include children -> <original pattern>/**
            .let {
                if (it.startsWith("*") && !it.contains("/")) {
                    "**/$it"
                } else {
                    if (includeChildren) slashJoin(it, "**") else it
                }
            }
            // collapse **/** (if any)
            .replace(Regex("(/[*][*]){2,}/"), "/**/").replace("**/**/", "**/").replace("/**/**", "/**")
        val canonicalBaseDir = when {
            restrictToBaseDir -> File(baseDir)
            prefix == "" -> if (pattern.startsWith("/")) File("/") else File(baseDir)
            prefix.startsWith("/") -> File(prefix) // absolute path
            else -> File(slashJoin(baseDir, prefix))
        }.canonicalPath
        val expectedPath = slashJoin(
            slash(canonicalBaseDir),
            if (restrictToBaseDir) pattern else pattern.substring(prefix.length)
        )
        state = expectedPath.split('/').map {
            if (it == "**") {
                State(Regex(".*"), true)
            } else {
                State(Regex(it.replace(Regex("([^a-zA-Z0-9 *])"), "\\\\$1").replace("*", ".*")), false)
            }
        }
    }

    private fun slashJoin(l: String, r: String) = "${l.removeSuffix("/")}/${r.removePrefix("/")}"

    // example:
    // path: b/b/c, glob: **/b/c
    //
    // +---+----+---+---+---+
    // |   | ** | b | c |   |
    // +---+----+---+---+---+
    // |   | x  |   |   |   |
    // | b | x  | x | o |   | <- next possible state
    // | b | x  | x | x |   |
    // | c | x  | o |   | x |
    // +---+----+---+---+---+
    //
    // time: O(n*m), space: O(m), where n - path.size, m - state.size
    //
    private fun matches0(path: List<String>, entire: Boolean): Boolean {
        val sizeOfState = state.size
        var match = BitSet(sizeOfState + 1).apply { set(0) }
        var nextMatch = BitSet(sizeOfState + 1) // next possible state
        for (p in path) {
            var stateIndex = -1
            while (true) {
                stateIndex = match.nextSetBit(stateIndex + 1)
                if (stateIndex == -1 || stateIndex == sizeOfState) {
                    break
                }
                val s = state[stateIndex]
                if (s.matches(p)) {
                    nextMatch[stateIndex + 1] = true
                }
                if (s.optional) {
                    nextMatch[stateIndex] = true
                    match.set(stateIndex + 1)
                }
            }
            if (nextMatch.isEmpty) {
                return false
            }
            nextMatch = match.apply { match = nextMatch } // swap
            nextMatch.clear()
        }
        return !entire || match[sizeOfState] ||
            (match[sizeOfState - 1] && state[sizeOfState - 1].optional)
    }

    fun matches(absolutePath: String, entire: Boolean = true): Boolean =
        matches0(absolutePath.split('/'), entire) != negate
}
