package net.nunnerycode.bukkit.mythicdrops.api.armorsets;

import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketEffect;

import java.util.List;

public interface ArmorSet {

	String getName();

	List<SocketEffect> getOneItemEffects();

	List<SocketEffect> getTwoItemEffects();

	List<SocketEffect> getThreeItemEffects();

	List<SocketEffect> getFourItemEffects();

	List<SocketEffect> getFiveItemEffects();

}
