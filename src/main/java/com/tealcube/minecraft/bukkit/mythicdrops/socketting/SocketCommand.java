package com.tealcube.minecraft.bukkit.mythicdrops.socketting;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketCommandRunner;

public final class SocketCommand {

    private final SocketCommandRunner runner;
    private final String command;

    public SocketCommand(String string) {
        if (string.length() < 6) {
            runner = SocketCommandRunner.DEFAULT_RUNNER;
            command = string.trim();
            return;
        }
        SocketCommandRunner run = SocketCommandRunner.fromName(string.substring(0, 6));
        if (run == null) {
            run = SocketCommandRunner.DEFAULT_RUNNER;
        }
        runner = run;
        String commandS;
        if (string.substring(0, runner.getName().length()).equalsIgnoreCase(runner.getName())) {
            commandS = string.substring(runner.getName().length(), string.length()).trim();
        } else {
            commandS = string.trim();
        }
        if (commandS.substring(0, 1).equalsIgnoreCase(":")) {
            commandS = commandS.substring(1, commandS.length()).trim();
        }
        command = commandS.trim();
    }

    public String toConfigString() {
        return runner.getName() + ":" + command.trim();
    }

    public String getCommand() {
        return command;
    }

    public SocketCommandRunner getRunner() {
        return runner;
    }

}
