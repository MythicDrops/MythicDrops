package com.conventnunnery.plugins.mythicdrops.permissions;

public enum PermissionNodes {
    /**
     * Permission node for "/md spawn"
     */
    COMMAND_SPAWN("mythicdrops.command.spawn"),
    /**
     * Permission node for "/md give"
     */
    COMMAND_GIVE("mythicdrops.command.give"),
    /**
     * Permission node for "/md reload"
     */
    COMMAND_RELOAD("mythicdrops.command.reload"),
    /**
     * Permission node for "/md custom"
     */
    COMMAND_CUSTOM("mythicdrops.command.custom");
    private final String node;

    private PermissionNodes(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
