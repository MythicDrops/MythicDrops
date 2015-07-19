package com.tealcube.minecraft.bukkit.mythicdrops.utils;

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


import com.google.common.collect.Sets;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;

import java.util.Set;

public final class ChatColorUtil {

    private ChatColorUtil() {
    }

    public static ChatColor getChatColorOrFallback(String string, ChatColor fallback) {
        String name = string;
        if (name == null) {
            name = fallback.name();
        }
        ChatColor chatColor;
        try {
            chatColor = ChatColor.valueOf(name);
        } catch (IllegalArgumentException exception) {
            chatColor = fallback;
        }
        return chatColor;
    }

    public static ChatColor getChatColor(String string) {
        if (string == null) {
            return null;
        }
        try {
            return ChatColor.valueOf(string);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static ChatColor getRandomChatColorWithoutColors(ChatColor... colors) {
        Set<ChatColor> colors1 = Sets.newHashSet(ChatColor.values());
        for (ChatColor c : colors) {
            if (colors1.contains(c)) {
                colors1.remove(c);
            }
        }
        return getRandomChatColorFromSet(colors1);
    }

    public static ChatColor getRandomChatColorFromSet(Set<ChatColor> chatColors) {
        ChatColor[] chatColors1 = chatColors.toArray(new ChatColor[chatColors.size()]);
        return chatColors1[RandomUtils.nextInt(chatColors1.length)];
    }

    public static ChatColor getRandomChatColor() {
        return getRandomChatColorFromSet(Sets.newHashSet(ChatColor.values()));
    }
}
