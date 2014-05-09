package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;
import org.bukkit.block.Chest;

public class ChestGenerateEvent extends MythicDropsEvent {

    private final Chest chest;

    public ChestGenerateEvent(Chest chest) {
        this.chest = chest;
    }

    public Chest getChest() {
        return chest;
    }

}
