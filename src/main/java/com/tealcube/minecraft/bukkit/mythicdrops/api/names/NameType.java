package com.tealcube.minecraft.bukkit.mythicdrops.api.names;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


public enum NameType {
    ENCHANTMENT_PREFIX("enchantment.prefix."), ENCHANTMENT_SUFFIX("enchantment.suffix."),
    ENCHANTMENT_LORE("enchantment.lore."), GENERAL_PREFIX("general.prefix"), GENERAL_SUFFIX(
            "general.suffix"),
    GENERAL_LORE("general.lore"), MATERIAL_PREFIX("material.prefix."), MATERIAL_SUFFIX(
            "material.suffix."),
    MATERIAL_LORE("material.lore."), TIER_PREFIX("tier.prefix."), TIER_SUFFIX("tier.suffix."),
    TIER_LORE("tier.lore."), SPECIFIC_MOB_NAME("mobname."), GENERAL_MOB_NAME("mobname"), ITEMTYPE_PREFIX(
            "itemtype.prefix."), ITEMTYPE_SUFFIX("itemtype.suffix."), ITEMTYPE_LORE("itemtype.lore.");
    private final String format;

    private NameType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
