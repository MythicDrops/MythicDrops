package net.nunnerycode.bukkit.mythicdrops.api.socketting;

public enum EffectTarget {
    SELF("SELF"), OTHER("OTHER"), NONE("NONE"), AREA("AREA"), AURA("AURA");
    private final String name;

    private EffectTarget(String name) {
        this.name = name;
    }

    public static EffectTarget getFromName(String name) {
        for (EffectTarget gt : EffectTarget.values()) {
            if (gt.getName().equalsIgnoreCase(name)) {
                return gt;
            }
        }
        return EffectTarget.NONE;
    }

    public String getName() {
        return name;
    }
}
