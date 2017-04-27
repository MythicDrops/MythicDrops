/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.settings;

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.IdentifyingSettings;
import java.util.ArrayList;
import java.util.List;

public final class MythicIdentifyingSettings implements IdentifyingSettings {

  private String identityTomeName;
  private List<String> identityTomeLore;
  private String unidentifiedItemName;
  private List<String> unidentifiedItemLore;

  public MythicIdentifyingSettings() {
    identityTomeLore = new ArrayList<>();
    unidentifiedItemLore = new ArrayList<>();
  }

  @Override
  public String getIdentityTomeName() {
    return identityTomeName;
  }

  public void setIdentityTomeName(String identityTomeName) {
    this.identityTomeName = identityTomeName;
  }

  @Override
  public List<String> getIdentityTomeLore() {
    return identityTomeLore;
  }

  public void setIdentityTomeLore(List<String> identityTomeLore) {
    this.identityTomeLore = identityTomeLore;
  }

  @Override
  public String getUnidentifiedItemName() {
    return unidentifiedItemName;
  }

  public void setUnidentifiedItemName(String unidentifiedItemName) {
    this.unidentifiedItemName = unidentifiedItemName;
  }

  @Override
  public List<String> getUnidentifiedItemLore() {
    return unidentifiedItemLore;
  }

  public void setUnidentifiedItemLore(List<String> unidentifiedItemLore) {
    this.unidentifiedItemLore = unidentifiedItemLore;
  }

  @Override
  @Deprecated
  public boolean isEnabled() {
    return true;
  }

  @Deprecated
  public void setEnabled(boolean enabled) {
    // do nothing
  }

}
