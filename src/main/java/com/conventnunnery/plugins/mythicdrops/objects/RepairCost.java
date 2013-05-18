package com.conventnunnery.plugins.mythicdrops.objects;

import org.bukkit.material.MaterialData;

public class RepairCost {

    private final MaterialData materialData;
    private final MaterialData repairItem;
    private final int amountRequired;
    private final double percentageRestored;

    public RepairCost(MaterialData materialData, MaterialData repairItem, int amountRequired,
                      double percentageRestored) {
        this.materialData = materialData;
        this.repairItem = repairItem;
        this.amountRequired = amountRequired;
        this.percentageRestored = percentageRestored;
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public MaterialData getRepairItem() {
        return repairItem;
    }

    public int getAmountRequired() {
        return amountRequired;
    }

    public double getPercentageRestored() {
        return percentageRestored;
    }


}
