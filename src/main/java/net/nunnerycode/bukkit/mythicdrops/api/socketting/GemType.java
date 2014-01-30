package net.nunnerycode.bukkit.mythicdrops.api.socketting;

public enum GemType {
  TOOL("TOOL"), ARMOR("ARMOR"), ANY("ANY");
  private final String name;

  private GemType(String name) {
    this.name = name;
  }

  public static GemType getFromName(String name) {
    for (GemType gt : GemType.values()) {
      if (gt.getName().equalsIgnoreCase(name)) {
        return gt;
      }
    }
    return ANY;
  }

  public String getName() {
    return name;
  }
}
