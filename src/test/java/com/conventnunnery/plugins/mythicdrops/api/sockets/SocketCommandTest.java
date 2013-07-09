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

import org.junit.Assert;
import org.junit.Test;

public class SocketCommandTest {
    @Test
    public void testGetCommand() throws Exception {
        String commandString = "PLAYER:stop";
        String commandResult = "stop";

        SocketCommand socketCommand = new SocketCommand(commandString);

        Assert.assertNotNull(commandString);
        Assert.assertNotNull(commandResult);
        Assert.assertNotNull(socketCommand);

        Assert.assertEquals(commandResult, socketCommand.getCommand());
    }

    @Test
    public void testGetRunner() throws Exception {
        String commandString = "PLAYER:stop";
        SocketCommandRunner runnerResult = SocketCommandRunner.PLAYER;

        SocketCommand socketCommand = new SocketCommand(commandString);

        Assert.assertNotNull(commandString);
        Assert.assertNotNull(runnerResult);
        Assert.assertNotNull(socketCommand);

        Assert.assertEquals(runnerResult, socketCommand.getRunner());
    }
}
