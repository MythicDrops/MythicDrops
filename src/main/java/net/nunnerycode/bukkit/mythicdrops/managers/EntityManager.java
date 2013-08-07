package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.events.CreatureEquippedWithItemStackEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class EntityManager {
    private final MythicDrops plugin;

    /**
     * Instantiates a new EntityManager.
     *
     * @param plugin the plugin
     */
    public EntityManager(MythicDrops plugin) {
        this.plugin = plugin;
    }

    public void equipEntity(LivingEntity entity, CustomItem customItem) {
        if (entity == null || customItem == null) {
            return;
        }
        ItemStack itemstack = customItem.toItemStack();
        equipEntity(entity, itemstack);
    }

    /**
     * Equip entity.
     *
     * @param entity    the entity
     * @param itemStack the itemstack
     */
    public void equipEntity(LivingEntity entity, ItemStack itemStack) {
        if (entity == null || itemStack == null) {
            return;
        }
        CreatureEquippedWithItemStackEvent cewise = new CreatureEquippedWithItemStackEvent(entity, itemStack);
        Bukkit.getPluginManager().callEvent(cewise);
        if (cewise.isCancelled()) {
            return;
        }
        ItemStack itemstack = cewise.getItemStack();
        if (itemstack.getType().name().toUpperCase().contains("BOOTS")) {
            cewise.getEntity().getEquipment().setBoots(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("LEGGINGS")) {
            cewise.getEntity().getEquipment().setLeggings(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("CHESTPLATE")) {
            cewise.getEntity().getEquipment().setChestplate(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("HELMET")) {
            cewise.getEntity().getEquipment().setHelmet(itemstack);
        } else {
            cewise.getEntity().getEquipment().setItemInHand(itemstack);
        }
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }
}
