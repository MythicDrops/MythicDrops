package net.nunnerycode.bukkit.mythicdrops.api.managers;

import com.modcrafting.diablodrops.name.NamesLoader;
import java.util.List;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public interface NameManager {

	void loadTierLore();

	void loadMaterialLore();

	void loadGeneralLore();

	void loadTierSuffixes();

	void loadMaterialSuffixes();

	void loadGeneralSuffixes();

	void loadTierPrefixes();

	void loadMaterialPrefixes();

	void loadGeneralPrefixes();

	void loadEnchantmentSuffixes();

	void loadEnchantmentPrefixes();

	void loadEnchantmentLore();

	Map<Enchantment, List<String>> getEnchantmentLore();

	Map<Tier, List<String>> getTierPrefixes();

	Map<Tier, List<String>> getTierSuffixes();

	void debugNames();

	NamesLoader getNamesLoader();

	List<String> getGeneralPrefixes();

	List<String> getGeneralSuffixes();

	Map<Material, List<String>> getMaterialPrefixes();

	Map<Material, List<String>> getMaterialSuffixes();

	String randomFormattedName(ItemStack itemStack, Tier tier);

	String randomEnchantmentSuffix(Enchantment enchantment);

	String randomEnchantmentPrefix(Enchantment enchantment);

	String getItemTypeName(MaterialData matData);

	String getEnchantmentTypeName(ItemStack itemStack);

	String getMythicMaterialName(MaterialData matData);

	String getMinecraftMaterialName(Material material);

	String randomTierSuffix(Tier tier);

	String randomTierPrefix(Tier tier);

	String randomMaterialSuffix(Material material);

	String randomMaterialPrefix(Material material);

	String randomGeneralSuffix();

	String randomGeneralPrefix();

	List<String> randomLore(Material material, Tier tier, Enchantment enchantment);

	List<String> randomEnchantmentLore(Enchantment enchantment);

	List<String> randomTierLore(Tier tier);

	List<String> randomMaterialLore(Material material);

	List<String> randomGeneralLore();

	List<String> getGeneralLore();

	Map<Tier, List<String>> getTierLore();

	Map<Material, List<String>> getMaterialLore();

	Map<Enchantment, List<String>> getEnchantmentPrefixes();

	Map<Enchantment, List<String>> getEnchantmentSuffixes();

	MythicDrops getPlugin();

}
