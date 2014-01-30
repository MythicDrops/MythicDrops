package net.nunnerycode.bukkit.mythicdrops.armorsets;

import net.nunnerycode.bukkit.mythicdrops.api.armorsets.ArmorSet;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketEffect;

import java.util.ArrayList;
import java.util.List;

public final class MythicArmorSet implements ArmorSet {

  private String name;
  private List<SocketEffect> oneItemEffects;
  private List<SocketEffect> twoItemEffects;
  private List<SocketEffect> threeItemEffects;
  private List<SocketEffect> fourItemEffects;
  private List<SocketEffect> fiveItemEffects;

  public MythicArmorSet(String name) {
    this.name = name;
    oneItemEffects = new ArrayList<>();
    twoItemEffects = new ArrayList<>();
    threeItemEffects = new ArrayList<>();
    fourItemEffects = new ArrayList<>();
    fiveItemEffects = new ArrayList<>();
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MythicArmorSet that = (MythicArmorSet) o;

    return !(name != null ? !name.equals(that.name) : that.name != null);

  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public List<SocketEffect> getOneItemEffects() {
    return oneItemEffects;
  }

  public void setOneItemEffects(List<SocketEffect> oneItemEffects) {
    this.oneItemEffects = oneItemEffects;
  }

  @Override
  public List<SocketEffect> getTwoItemEffects() {
    return twoItemEffects;
  }

  public void setTwoItemEffects(List<SocketEffect> twoItemEffects) {
    this.twoItemEffects = twoItemEffects;
  }

  @Override
  public List<SocketEffect> getThreeItemEffects() {
    return threeItemEffects;
  }

  public void setThreeItemEffects(List<SocketEffect> threeItemEffects) {
    this.threeItemEffects = threeItemEffects;
  }

  @Override
  public List<SocketEffect> getFourItemEffects() {
    return fourItemEffects;
  }

  public void setFourItemEffects(List<SocketEffect> fourItemEffects) {
    this.fourItemEffects = fourItemEffects;
  }

  @Override
  public List<SocketEffect> getFiveItemEffects() {
    return fiveItemEffects;
  }

  public void setFiveItemEffects(List<SocketEffect> fiveItemEffects) {
    this.fiveItemEffects = fiveItemEffects;
  }

}
