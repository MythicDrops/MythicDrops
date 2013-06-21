package com.conventnunnery.plugins.mythicdrops.configuration;

import com.conventnunnery.libraries.config.IConfigurationFile;

public enum MythicConfigurationFile implements IConfigurationFile {
    CONFIG("config.yml"), LANGUAGE("language.yml"), TIER("tier.yml"), CUSTOMITEM("customItems.yml"),
    SOCKETGEM("socketgem.yml"), DROPRATES("dropRates.yml"), ITEMGROUPS("itemGroups.yml"),
    REPAIRCOSTS("repairCosts.yml");
    private final String filename;

    private MythicConfigurationFile(String path) {
        this.filename = path;
    }

    @Override
    public String getFileName() {
        return filename;
    }
}
