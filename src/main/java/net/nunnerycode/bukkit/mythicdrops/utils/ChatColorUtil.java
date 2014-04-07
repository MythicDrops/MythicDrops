package net.nunnerycode.bukkit.mythicdrops.utils;

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
