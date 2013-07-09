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

package com.conventnunnery.plugins.mythicdrops.api.sockets;

public class SocketCommand {
    private final SocketCommandRunner runner;
    private final String command;

    public SocketCommand(String string) {
        String s = string;
        if (s.length() < 6) {
            runner = SocketCommandRunner.DEFAULT_RUNNER;
            command = s;
            return;
        }
        SocketCommandRunner run = SocketCommandRunner.fromName(s.substring(0, 6));
        if (run == null) {
            run = SocketCommandRunner.DEFAULT_RUNNER;
        }
        runner = run;
        String commandS = s.substring(6, s.length());
        if (commandS.substring(0, 1).equalsIgnoreCase(":")) {
            commandS = s.substring(7, s.length());
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
