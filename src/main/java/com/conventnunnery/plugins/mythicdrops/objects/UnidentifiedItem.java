/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * Some code courtesy of deathmarine
 */

package com.conventnunnery.plugins.mythicdrops.objects;

import com.conventnunnery.plugins.conventlib.utils.CollectionUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.parents.MythicRegularItem;
import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

public class UnidentifiedItem extends MythicRegularItem {
    public UnidentifiedItem(final MaterialData mat) {
        super(mat, ChatColor.WHITE + MythicDrops.getInstance().getLanguageManager().getMessage("items.unidentified" +
                ".name") + ChatColor.WHITE,
                CollectionUtils
                .toStringArray(MythicDrops.getInstance().getLanguageManager().getStringList("items.unidentified.lore",
                        new String[][]{{"%identity-tome-name%",
                                MythicDrops.getInstance().getLanguageManager()
                                        .getMessage("items.identity-tome.name")}})));
    }
}
