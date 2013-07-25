package net.nunnerycode.bukkit.mythicdrops.api.sockets;

public enum SocketActionType {
    FIRE("Fire"), BLINK("Blink");
    private final String name;

    private SocketActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
