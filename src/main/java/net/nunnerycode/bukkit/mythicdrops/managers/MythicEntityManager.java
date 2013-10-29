package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.events.CreatureEquippedWithItemStackEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class MythicEntityManager {
    private final MythicDropsPlugin plugin;

    /**
     * Instantiates a new EntityManager.
     *
     * @param plugin the plugin
     */
    public MythicEntityManager(MythicDropsPlugin plugin) {
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
			cewise.getEntity().getEquipment().setBootsDropChance(0.0F);
        } else if (itemstack.getType().name().toUpperCase().contains("LEGGINGS")) {
            cewise.getEntity().getEquipment().setLeggings(itemstack);
			cewise.getEntity().getEquipment().setLeggingsDropChance(0.0F);
        } else if (itemstack.getType().name().toUpperCase().contains("CHESTPLATE")) {
            cewise.getEntity().getEquipment().setChestplate(itemstack);
			cewise.getEntity().getEquipment().setChestplateDropChance(0.0F);
        } else if (itemstack.getType().name().toUpperCase().contains("HELMET")) {
            cewise.getEntity().getEquipment().setHelmet(itemstack);
			cewise.getEntity().getEquipment().setHelmetDropChance(0.0F);
        } else {
            cewise.getEntity().getEquipment().setItemInHand(itemstack);
			cewise.getEntity().getEquipment().setItemInHandDropChance(0.0F);
        }
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public MythicDropsPlugin getPlugin() {
        return plugin;
    }
}