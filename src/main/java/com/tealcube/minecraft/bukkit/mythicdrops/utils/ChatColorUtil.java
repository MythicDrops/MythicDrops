/*
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
package com.tealcube.minecraft.bukkit.mythicdrops.utils;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.ChatColor;

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
        return chatColors1[RandomUtils.nextInt(0, chatColors1.length)];
    }

    public static ChatColor getRandomChatColor() {
        return getRandomChatColorFromSet(Sets.newHashSet(ChatColor.values()));
    }
}
