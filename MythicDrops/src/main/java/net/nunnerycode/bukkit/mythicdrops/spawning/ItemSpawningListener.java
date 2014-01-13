package net.nunnerycode.bukkit.mythicdrops.spawning;

import com.google.common.base.Joiner;
import mkremins.fanciful.FancyMessage;
import net.nunnerycode.bukkit.libraries.ivory.utils.JSONUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.EntityDyingEvent;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketItem;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.CustomItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.EntityUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ItemSpawningListener implements Listener {

	private MythicDrops mythicDrops;

	public ItemSpawningListener(MythicDropsPlugin mythicDrops) {
		this.mythicDrops = mythicDrops;
	}

	public MythicDrops getMythicDrops() {
		return mythicDrops;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawnEventLowest(CreatureSpawnEvent event) {
		if (event.getEntity() instanceof Player) {
			return;
		}
		if (mythicDrops.getCreatureSpawningSettings().isBlankMobSpawnEnabled()) {
			if (event.getEntity() instanceof Skeleton) {
				event.getEntity().getEquipment().clear();
				if (mythicDrops.getCreatureSpawningSettings().isBlankMobSpawnSkeletonsSpawnWithBows()) {
					event.getEntity().getEquipment().setItemInHand(new ItemStack(Material.BOW, 1));
				}
			} else {
				event.getEntity().getEquipment().clear();
			}
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
				&& mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
			event.getEntity().setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
				&& mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
			event.getEntity().setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
				mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
			event.getEntity().setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
			return;
		}
		if (event.getEntity().getLocation().getY() > mythicDrops.getCreatureSpawningSettings().getSpawnHeightLimit(event.getEntity
				().getWorld().getName())) {
			event.getEntity().setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
			return;
		}
		event.getEntity().setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		if (event.getEntity() instanceof Player || event.isCancelled()) {
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
				&& mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
				&& mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
				mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
			return;
		}
		if (mythicDrops.getCreatureSpawningSettings().getSpawnHeightLimit(event.getEntity().getWorld().getName()) <= event
				.getEntity().getLocation().getY()) {
			return;
		}
		if (!mythicDrops.getCreatureSpawningSettings().isGiveMobsEquipment()) {
			return;
		}
		double chance = mythicDrops.getCreatureSpawningSettings().getGlobalSpawnChance() * mythicDrops.getCreatureSpawningSettings()
				.getEntityTypeChanceToSpawn(event.getEntityType());
		if (mythicDrops.getCreatureSpawningSettings().isOnlyCustomItemsSpawn()) {
			if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn() && RandomUtils.nextDouble() < mythicDrops
					.getCreatureSpawningSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance().isEmpty()) {
				for (int i = 0; i < 5; i++) {
					if (RandomUtils.nextDouble() < chance) {
						EntityUtil.equipEntity(event.getEntity(), CustomItemMap.getInstance().getRandomWithChance()
								.toItemStack());
						chance *= 0.5;
						continue;
					}
					break;
				}
			}
			return;
		}

		if (mythicDrops.getCreatureSpawningSettings().isGiveMobsNames()) {
			String generalName = NameMap.getInstance().getRandom(NameType.MOB_NAME, "");
			String specificName = NameMap.getInstance().getRandom(NameType.MOB_NAME, "." + event.getEntityType().name
					());
			event.getEntity().setCustomName(specificName != null ? specificName : generalName);
		}

		if (mythicDrops.getCreatureSpawningSettings().getEntityTypeChanceToSpawn(event.getEntityType()) <= 0 &&
				mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers(event.getEntityType()).isEmpty()) {
			return;
		}
		for (int i = 0; i < 5; i++) {
			if (RandomUtils.nextDouble() < chance) {
				Tier tier = getTier("*", event.getEntity());
				if (tier == null) {
					continue;
				}
				try {
					ItemStack itemStack = new MythicDropBuilder().inWorld(event.getEntity()
							.getWorld()).useDurability(true).withTier(tier).withItemGenerationReason
							(ItemGenerationReason.MONSTER_SPAWN).build();
					EntityUtil.equipEntity(event.getEntity(), itemStack);
				} catch (Exception e) {
					continue;
				}
				chance *= 0.5;
				continue;
			}
			break;
		}
		if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn() && RandomUtils.nextDouble() < mythicDrops
				.getCreatureSpawningSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance().isEmpty()) {
			for (int i = 0; i < 5; i++) {
				if (RandomUtils.nextDouble() < chance) {
					EntityUtil.equipEntity(event.getEntity(), CustomItemMap.getInstance().getRandomWithChance()
							.toItemStack());
					chance *= 0.5;
					continue;
				}
				break;
			}
		}
	}

	private Tier getTier(String tierName, LivingEntity livingEntity) {
		Tier tier;
		if (tierName.equals("*")) {
			tier = TierUtil.randomTierWithChance(mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers
					(livingEntity.getType()));
			if (tier == null) {
				tier = TierUtil.randomTierWithChance(mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers
						(livingEntity.getType()));
			}
		} else {
			tier = TierMap.getInstance().get(tierName.toLowerCase());
			if (tier == null) {
				tier = TierMap.getInstance().get(tierName);
			}
		}
		return tier;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player || event.getEntity().getLastDamageCause() == null || event.getEntity()
				.getLastDamageCause().isCancelled()) {
			return;
		}

		EntityDamageEvent.DamageCause damageCause = event.getEntity().getLastDamageCause().getCause();

		if (damageCause == EntityDamageEvent.DamageCause.CONTACT || damageCause == EntityDamageEvent.DamageCause
				.SUFFOCATION || damageCause == EntityDamageEvent.DamageCause.FALL || damageCause == EntityDamageEvent
				.DamageCause.FIRE_TICK || damageCause == EntityDamageEvent.DamageCause.MELTING || damageCause ==
				EntityDamageEvent.DamageCause.LAVA || damageCause == EntityDamageEvent.DamageCause.DROWNING ||
				damageCause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || damageCause == EntityDamageEvent
				.DamageCause.BLOCK_EXPLOSION || damageCause == EntityDamageEvent.DamageCause.VOID || damageCause ==
				EntityDamageEvent.DamageCause.LIGHTNING || damageCause == EntityDamageEvent.DamageCause.SUICIDE ||
				damageCause == EntityDamageEvent.DamageCause.STARVATION || damageCause == EntityDamageEvent
				.DamageCause.WITHER || damageCause == EntityDamageEvent.DamageCause.FALLING_BLOCK || damageCause ==
				EntityDamageEvent.DamageCause.CUSTOM) {
			return;
		}

		if (mythicDrops.getCreatureSpawningSettings().isGiveMobsEquipment()) {
			handleEntityDyingWithGive(event);
		} else {
			handleEntityDyingWithoutGive(event);
		}
	}

	private void handleEntityDyingWithGive(EntityDeathEvent event) {
		List<ItemStack> newDrops = new ArrayList<>();

		ItemStack[] array = new ItemStack[5];
		System.arraycopy(event.getEntity().getEquipment().getArmorContents(), 0, array, 0, 4);
		array[4] = event.getEntity().getEquipment().getItemInHand();

		event.getEntity().getEquipment().setBootsDropChance(0.0F);
		event.getEntity().getEquipment().setLeggingsDropChance(0.0F);
		event.getEntity().getEquipment().setChestplateDropChance(0.0F);
		event.getEntity().getEquipment().setHelmetDropChance(0.0F);
		event.getEntity().getEquipment().setItemInHandDropChance(0.0F);

		for (ItemStack is : array) {
			if (is == null || is.getType() == Material.AIR) {
				continue;
			}
			if (!is.hasItemMeta()) {
				continue;
			}
			CustomItem ci;
			try {
				ci = CustomItemUtil.getCustomItemFromItemStack(is);
			} catch (NullPointerException e) {
				ci = null;
			}
			if (ci != null) {
				if (RandomUtils.nextDouble() < ci.getChanceToDropOnDeath()) {
					newDrops.add(ci.toItemStack());
					continue;
				}
			}
			Tier tier = TierUtil.getTierFromItemStack(is, mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers(event.getEntityType()));
			if (tier == null) {
				continue;
			}

			String displayName = WordUtils.capitalizeFully(Joiner.on(" ").join(is.getType().name().split("_")));
			List<String> lore = new ArrayList<>();
			Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
			if (is.hasItemMeta()) {
				if (is.getItemMeta().hasDisplayName()) {
					displayName = is.getItemMeta().getDisplayName();
				}
				if (is.getItemMeta().hasLore()) {
					lore = is.getItemMeta().getLore();
				}
				if (is.getItemMeta().hasEnchants()) {
					enchantments = is.getItemMeta().getEnchants();
				}
			}

			if (tier.isBroadcastOnFind() && event.getEntity().getKiller() != null) {
				String locale = mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
						".found-item-broadcast", new String[][]{{"%receiver%", event.getEntity().getKiller()
						.getName()}});
				String[] messages = locale.split("%item%");
				if (Bukkit.getServer().getClass().getPackage().getName().equals("org.bukkit.craftbukkit" +
						".v1_7_R1")) {
					FancyMessage fancyMessage = new FancyMessage("");
					for (int i1 = 0; i1 < messages.length; i1++) {
						String key = messages[i1];
						if (i1 < messages.length - 1) {
							fancyMessage.then(key).then(displayName).itemTooltip(JSONUtils.toJSON(is.getData()
									.getItemTypeId(), is.getData().getData(), displayName, lore, enchantments));
						} else {
							fancyMessage.then(key);
						}
					}
					for (Player player : event.getEntity().getWorld().getPlayers()) {
						fancyMessage.send(player);
					}
				} else {
					for (Player player : event.getEntity().getWorld().getPlayers()) {
						player.sendMessage(locale.replace("%item%", displayName));
					}
				}
			}

			if (RandomUtils.nextDouble() < getTierDropChance(tier, event.getEntity().getWorld().getName())) {
				ItemStack newItemStack = is.getData().toItemStack(is.getAmount());
				newItemStack.setItemMeta(is.getItemMeta().clone());
				newItemStack.setDurability(ItemStackUtil.getDurabilityForMaterial(is.getType(),
						tier.getMinimumDurabilityPercentage(), tier.getMaximumDurabilityPercentage()));
				newDrops.add(newItemStack);
			}
		}

		EntityDyingEvent ede = new EntityDyingEvent(event.getEntity(), array, newDrops);
		Bukkit.getPluginManager().callEvent(ede);

		Location location = event.getEntity().getLocation();

		for (ItemStack itemstack : ede.getEquipmentDrops()) {
			if (itemstack.getData().getItemTypeId() == 0) {
				continue;
			}
			location.getWorld().dropItemNaturally(location, itemstack);
		}
	}

	private double getTierDropChance(Tier t, String worldName) {
		if (t.getWorldDropChanceMap().containsKey(worldName)) {
			return t.getWorldDropChanceMap().get(worldName);
		}
		if (t.getWorldDropChanceMap().containsKey("default")) {
			return t.getWorldDropChanceMap().get("default");
		}
		return 1.0;
	}

	private void handleEntityDyingWithoutGive(EntityDeathEvent event) {
		List<ItemStack> newDrops = new ArrayList<>();
		ItemStack[] array = new ItemStack[5];
		System.arraycopy(event.getEntity().getEquipment().getArmorContents(), 0, array, 0, 4);
		array[4] = event.getEntity().getEquipment().getItemInHand();
		double chance = mythicDrops.getCreatureSpawningSettings().getGlobalSpawnChance() * mythicDrops.getCreatureSpawningSettings()
				.getEntityTypeChanceToSpawn(event.getEntityType());
		if (mythicDrops.getCreatureSpawningSettings().isOnlyCustomItemsSpawn()) {
			if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn() && RandomUtils.nextDouble() < mythicDrops
					.getCreatureSpawningSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance().isEmpty()) {
				for (int i = 0; i < 5; i++) {
					if (RandomUtils.nextDouble() < chance) {
						newDrops.add(CustomItemMap.getInstance().getRandomWithChance().toItemStack());
						chance *= 0.5;
						continue;
					}
					break;
				}
			}
			return;
		}
		if (mythicDrops.getCreatureSpawningSettings().getEntityTypeChanceToSpawn(event.getEntityType()) <= 0 &&
				mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers(event.getEntityType()).isEmpty()) {
			return;
		}
		for (int i = 0; i < 5; i++) {
			if (RandomUtils.nextDouble() < chance) {
				Tier tier = getTier("*", event.getEntity());
				if (tier == null) {
					continue;
				}
				try {
					ItemStack itemStack = new MythicDropBuilder().inWorld(event.getEntity().getWorld()).useDurability(true).
							withTier(tier).withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN).build();
					newDrops.add(itemStack);

					String displayName = WordUtils.capitalizeFully(Joiner.on(" ").join(itemStack.getType().name().split("_")));
					List<String> lore = new ArrayList<>();
					Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
					if (itemStack.hasItemMeta()) {
						if (itemStack.getItemMeta().hasDisplayName()) {
							displayName = itemStack.getItemMeta().getDisplayName();
						}
						if (itemStack.getItemMeta().hasLore()) {
							lore = itemStack.getItemMeta().getLore();
						}
						if (itemStack.getItemMeta().hasEnchants()) {
							enchantments = itemStack.getItemMeta().getEnchants();
						}
					}

					if (tier.isBroadcastOnFind() && event.getEntity().getKiller() != null) {
						String locale = mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
								".found-item-broadcast", new String[][]{{"%receiver%", event.getEntity().getKiller()
								.getName()}});
						String[] messages = locale.split("%item%");
						if (Bukkit.getServer().getClass().getPackage().getName().equals("org.bukkit.craftbukkit" +
								".v1_7_R1")) {
							FancyMessage fancyMessage = new FancyMessage("");
							for (int i1 = 0; i1 < messages.length; i1++) {
								String key = messages[i1];
								if (i1 < messages.length - 1) {
									fancyMessage.then(key).then(displayName).itemTooltip(JSONUtils.toJSON(itemStack.getData()
											.getItemTypeId(), itemStack.getData().getData(), displayName, lore, enchantments));
								} else {
									fancyMessage.then(key);
								}
							}
							for (Player player : event.getEntity().getWorld().getPlayers()) {
								fancyMessage.send(player);
							}
						} else {
							for (Player player : event.getEntity().getWorld().getPlayers()) {
								player.sendMessage(locale.replace("%item%", displayName));
							}
						}
					}
				} catch (Exception e) {
					continue;
				}
				chance *= 0.5;
				continue;
			}
			break;
		}
		if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn() && RandomUtils.nextDouble() < mythicDrops
				.getCreatureSpawningSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance().isEmpty()) {
			for (int i = 0; i < 5; i++) {
				if (RandomUtils.nextDouble() < chance) {
					newDrops.add(CustomItemMap.getInstance().getRandomWithChance().toItemStack());
					chance *= 0.5;
					continue;
				}
				break;
			}
		}

		EntityDyingEvent ede = new EntityDyingEvent(event.getEntity(), array, newDrops);
		Bukkit.getPluginManager().callEvent(ede);

		Location location = event.getEntity().getLocation();

		for (ItemStack itemstack : ede.getEquipmentDrops()) {
			if (itemstack.getData().getItemTypeId() == 0) {
				continue;
			}
			location.getWorld().dropItemNaturally(location, itemstack);
		}
	}

	@EventHandler
	public void onEntityDyingEvent(EntityDyingEvent event) {
		String replaceString = mythicDrops.getSockettingSettings().getSocketGemName().replace('&',
				'\u00A7').replace("\u00A7\u00A7", "&").replaceAll("%(?s)(.*?)%", "").replaceAll("\\s+", " ");
		String[] splitString = ChatColor.stripColor(replaceString).split(" ");
		for (ItemStack is : event.getEquipment()) {
			if (is.getType() == Material.AIR) {
				continue;
			}
			if (!is.hasItemMeta()) {
				continue;
			}
			ItemMeta im = is.getItemMeta();
			if (!im.hasDisplayName()) {
				continue;
			}
			String displayName = im.getDisplayName();
			String colorlessName = ChatColor.stripColor(displayName);

			for (String s : splitString) {
				if (colorlessName.contains(s)) {
					colorlessName = colorlessName.replace(s, "");
				}
			}

			colorlessName = colorlessName.replaceAll("\\s+", " ").trim();

			SocketGem socketGem = SocketGemUtil.getSocketGemFromName(colorlessName);
			if (socketGem == null) {
				continue;
			}
			if (is.isSimilar(new SocketItem(is.getData(), socketGem))) {
				event.getEquipmentDrops().add(new SocketItem(is.getData(), socketGem));
			}
		}
	}


}
