package net.nunnerycode.bukkit.mythicdrops.sockets;

public enum SocketCommandRunner {
	PLAYER("PLAYER"), CONSOLE("CONSOLE");
	public static final SocketCommandRunner DEFAULT_RUNNER = CONSOLE;
	private final String name;

	private SocketCommandRunner(String name) {
		this.name = name;
	}

	public static SocketCommandRunner fromName(String name) {
		for (SocketCommandRunner runner : values()) {
			if (runner.getName().equalsIgnoreCase(name) || runner.name().equalsIgnoreCase(name)) {
				return runner;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}
}
