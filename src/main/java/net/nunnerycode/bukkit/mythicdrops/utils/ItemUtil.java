package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.*;

public final class ItemUtil {

    private static MythicDrops plugin = MythicDropsPlugin.getInstance();

    private ItemUtil() {
        // do nothing
    }

    public static Material getRandomMaterialFromCollection(
            Collection<Material> collection) {
        if (collection == null) {
            return Material.AIR;
        }
        Material[] array = collection.toArray(new Material[collection.size()]);
        return array[RandomUtils.nextInt(array.length)];
    }

    public static MaterialData getRandomMaterialDataFromCollection(
            Collection<MaterialData> collection) {
        if (collection == null) {
            return new MaterialData(Material.AIR);
        }
        MaterialData[] array = collection.toArray(new MaterialData[collection.size()]);
        return array[RandomUtils.nextInt(array.length)];
    }

    /**
     * Gets a {@link Collection} of {@link Tier}s that the given {@link Material} can be used by.
     *
     * @param material Material to check
     * @return All Tiers that can use the given Material
     */
    public static Collection<Tier> getTiersFromMaterial(Material material) {
        List<Tier> list = new ArrayList<>();
        if (material == null) {
            return list;
        }
        for (Tier t : TierMap.getInstance().values()) {
            Collection<Material> materials = getMaterialsFromTier(t);
            for (Material m : materials) {
                if (m == material) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * Gets a {@link Collection} of {@link Tier}s that the given {@link MaterialData} can be used by.
     *
     * @param materialData MaterialData to check
     * @return All Tiers that can use the given MaterialData
     */
    @Deprecated
    public static Collection<Tier> getTiersFromMaterialData(MaterialData materialData) {
        return getTiersFromMaterial(materialData != null ? materialData.getItemType() : Material.AIR);
    }

    /**
     * Gets a {@link Collection} of {@link Material}s that the given {@link Tier} contains.
     *
     * @param tier Tier to check
     * @return All Materials for the given Tier
     */
    public static Collection<Material> getMaterialsFromTier(Tier tier) {
        if (tier == null) {
            return new ArrayList<>();
        }
        List<String> idList = new ArrayList<>(tier.getAllowedItemIds());
        for (String itemType : tier.getAllowedItemGroups()) {
            if (plugin.getConfigSettings().getItemTypesWithIds().containsKey(itemType.toLowerCase())) {
                idList.addAll(plugin.getConfigSettings().getItemTypesWithIds().get(itemType.toLowerCase()));
            }
            if (plugin.getConfigSettings().getMaterialTypesWithIds()
                    .containsKey(itemType.toLowerCase())) {
                idList.addAll(
                        plugin.getConfigSettings().getMaterialTypesWithIds().get(itemType.toLowerCase()));
            }
        }
        for (String itemType : tier.getDisallowedItemGroups()) {
            if (plugin.getConfigSettings().getItemTypesWithIds().containsKey(itemType.toLowerCase())) {
                idList.removeAll(
                        plugin.getConfigSettings().getItemTypesWithIds().get(itemType.toLowerCase()));
            }
            if (plugin.getConfigSettings().getMaterialTypesWithIds()
                    .containsKey(itemType.toLowerCase())) {
                idList.removeAll(
                        plugin.getConfigSettings().getMaterialTypesWithIds().get(itemType.toLowerCase()));
            }
        }
        idList.removeAll(tier.getDisallowedItemIds());
        Set<Material> materials = new HashSet<>();
        for (String s : idList) {
            Material material = Material.getMaterial(s);
            if (material == Material.AIR) {
                continue;
            }
            materials.add(material);
        }
        return materials;
    }

    /**
     * Gets a {@link Collection} of {@link MaterialData}s that the given {@link Tier} contains.
     *
     * @param tier Tier to check
     * @return All MaterialDatas for the given Tier
     */
    @Deprecated
    public static Collection<MaterialData> getMaterialDatasFromTier(Tier tier) {
        if (tier == null) {
            return new ArrayList<>();
        }
        List<MaterialData> materialDatas = new ArrayList<>();
        Collection<Material> materials = getMaterialsFromTier(tier);
        for (Material m : materials) {
            materialDatas.add(new MaterialData(m));
        }
        return materialDatas;
    }

    /**
     * Returns true if the given item type is a kind of armor.
     *
     * @param itemType item type to check
     * @return if item type is a kind of armor
     */
    public static boolean isArmor(String itemType) {
        return itemType != null && plugin.getConfigSettings().getArmorTypes()
                .contains(itemType.toLowerCase());
    }

    /**
     * Returns true if the given item type is a kind of tool.
     *
     * @param itemType item type to check
     * @return if item type is a kind of tool
     */
    public static boolean isTool(String itemType) {
        return itemType != null && plugin.getConfigSettings().getToolTypes()
                .contains(itemType.toLowerCase());
    }

    /**
     * Returns true if the given material type is a kind of material.
     *
     * @param materialType material type to check
     * @return if item type is a kind of material
     */
    public static boolean isMaterial(String materialType) {
        return materialType != null && plugin.getConfigSettings().getMaterialTypes()
                .contains(materialType.toLowerCase());
    }

    /**
     * Gets the item type from the given {@link Material}.
     *
     * @param material Material to check
     * @return item type
     */
    public static String getItemTypeFromMaterial(Material material) {
        Map<String, List<String>> ids = plugin.getConfigSettings().getItemTypesWithIds();
        for (Map.Entry<String, List<String>> e : ids.entrySet()) {
            if (e.getValue().contains(material.name())) {
                if (plugin.getConfigSettings().getMaterialTypes().contains(e.getKey())) {
                    continue;
                }
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the item type from the given {@link MaterialData}.
     *
     * @param materialData MaterialData to check
     * @return item type
     */
    @Deprecated
    public static String getItemTypeFromMaterialData(MaterialData materialData) {
        Validate.notNull(materialData, "MaterialData cannot be null");
        return getItemTypeFromMaterial(materialData.getItemType());
    }

    /**
     * Gets the material type from the given {@link Material}.
     *
     * @param material Material to check
     * @return material type
     */
    public static String getMaterialTypeFromMaterial(Material material) {
        Map<String, List<String>> ids = plugin.getConfigSettings().getMaterialTypesWithIds();
        for (Map.Entry<String, List<String>> e : ids.entrySet()) {
            if (e.getValue().contains(material.name())) {
                if (plugin.getConfigSettings().getArmorTypes().contains(e.getKey()) || plugin
                        .getConfigSettings().getToolTypes().contains(e.getKey())) {
                    continue;
                }
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the material type from the given {@link MaterialData}.
     *
     * @param materialData MaterialData to check
     * @return material type
     */
    @Deprecated
    public static String getMaterialTypeFromMaterialData(MaterialData materialData) {
        Validate.notNull(materialData, "MaterialData cannot be null");
        return getMaterialTypeFromMaterial(materialData.getItemType());
    }

    /**
     * Checks if a Material has a particular item type.
     *
     * @param itemType item type to check
     * @param material Material to check
     * @return if item type matches item type of MaterialData
     */
    public static boolean isItemType(String itemType, Material material) {
        return itemType != null && material != null && getMaterialsFromItemType(itemType)
                .contains(material);
    }

    /**
     * Checks if a MaterialData has a particular item type.
     *
     * @param itemType     item type to check
     * @param materialData MaterialData to check
     * @return if item type matches item type of MaterialData
     */
    @Deprecated
    public static boolean isItemType(String itemType, MaterialData materialData) {
        return isItemType(itemType, materialData != null ? materialData.getItemType() : Material.AIR);
    }

    /**
     * Gets a {@link java.util.Collection} of {@link org.bukkit.material.MaterialData}s from an item type.
     *
     * @param itemType type of item
     * @return All MaterialDatas associated with the item type
     */
    public static Collection<Material> getMaterialsFromItemType(String itemType) {
        List<Material> list = new ArrayList<>();
        if (itemType == null) {
            return list;
        }
        List<String> ids = plugin.getConfigSettings().getItemTypesWithIds().get(itemType.toLowerCase());
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            Material material = Material.getMaterial(id);
            if (material == Material.AIR) {
                continue;
            }
            list.add(material);
        }
        return list;
    }

    /**
     * Gets a {@link java.util.Collection} of {@link org.bukkit.material.MaterialData}s from an item type.
     *
     * @param itemType type of item
     * @return All MaterialDatas associated with the item type
     */
    @Deprecated
    public static Collection<MaterialData> getMaterialDatasFromItemType(String itemType) {
        List<MaterialData> list = new ArrayList<>();
        if (itemType == null) {
            return list;
        }
        Collection<Material> collection = getMaterialsFromItemType(itemType);
        for (Material m : collection) {
            list.add(new MaterialData(m));
        }
        return list;
    }

    /**
     * Checks if a Material's item type matches a specified item type.
     *
     * @param materialType item type to check
     * @param material     MaterialData to check
     * @return if material type matches material type of MaterialData
     */
    public static boolean isMaterialType(String materialType, Material material) {
        return materialType != null && material != null && getMaterialsFromMaterialType(
                materialType).contains(material);
    }

    /**
     * Checks if a MaterialData's item type matches a specified item type.
     *
     * @param materialType item type to check
     * @param materialData MaterialData to check
     * @return if material type matches material type of MaterialData
     */
    @Deprecated
    public static boolean isMaterialType(String materialType, MaterialData materialData) {
        return isMaterialType(materialType, materialData != null ? materialData.getItemType() :
                Material.AIR);
    }

    /**
     * Gets a {@link Collection} of {@link Material}s from a material type.
     *
     * @param materialType type of material
     * @return All MaterialDatas associated with the material type
     */
    public static Collection<Material> getMaterialsFromMaterialType(String materialType) {
        List<Material> list = new ArrayList<>();
        if (materialType == null) {
            return list;
        }
        List<String> ids =
                plugin.getConfigSettings().getMaterialTypesWithIds().get(materialType.toLowerCase());
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            Material material = Material.getMaterial(id);
            if (material == Material.AIR) {
                continue;
            }
            list.add(material);
        }
        return list;
    }

    /**
     * Gets a {@link Collection} of {@link MaterialData}s from a material type.
     *
     * @param materialType type of material
     * @return All MaterialDatas associated with the material type
     */
    @Deprecated
    public static Collection<MaterialData> getMaterialDatasFromMaterialType(String materialType) {
        List<MaterialData> list = new ArrayList<>();
        if (materialType == null) {
            return list;
        }
        Collection<Material> collection = getMaterialsFromMaterialType(materialType);
        for (Material m : collection) {
            list.add(new MaterialData(m));
        }
        return list;
    }

}
