package net.nunnerycode.bukkit.mythicdrops.api.items;

public enum ItemGenerationReason {
	/**
	 * Whenever mobs spawn
	 */
    MONSTER_SPAWN,
	/**
	 * Whenever spawned by command
	 */
	COMMAND,
	/**
	 * For use by external plugins
	 */
	EXTERNAL
}