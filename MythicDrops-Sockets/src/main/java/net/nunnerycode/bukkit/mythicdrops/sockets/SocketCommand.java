package net.nunnerycode.bukkit.mythicdrops.sockets;

public class SocketCommand {
	private final SocketCommandRunner runner;
	private final String command;

	public String toConfigString() {
		return runner.getName() + ":" + command.trim();
	}

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

	public String getCommand() {
		return command;
	}

	public SocketCommandRunner getRunner() {
		return runner;
	}
}
