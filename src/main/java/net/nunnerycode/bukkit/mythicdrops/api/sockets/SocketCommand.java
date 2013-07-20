/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.nunnerycode.bukkit.mythicdrops.api.sockets;

import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketCommandRunner;

public class SocketCommand {
    private final SocketCommandRunner runner;
    private final String command;

    public SocketCommand(String string) {
        if (string.length() < 6) {
            runner = SocketCommandRunner.DEFAULT_RUNNER;
            command = string;
            return;
        }
        SocketCommandRunner run = SocketCommandRunner.fromName(string.substring(0, 6));
        if (run == null) {
            run = SocketCommandRunner.DEFAULT_RUNNER;
        }
        runner = run;
        String commandS = string.substring(6, string.length());
        if (commandS.substring(0, 1).equalsIgnoreCase(":")) {
            commandS = string.substring(7, string.length());
        }
        command = commandS;
    }

    public String getCommand() {
        return command;
    }

    public SocketCommandRunner getRunner() {
        return runner;
    }
}
