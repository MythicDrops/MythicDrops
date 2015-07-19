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


import java.nio.charset.Charset;

public final class StringUtil {

    private StringUtil() {
        // do nothing
    }

    public static String convertBytesToCharset(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }

    public static byte[] convertStringToCharset(String string, Charset charset) {
        return string.getBytes(charset);
    }

    public static String replaceArgs(String string, String[][] args) {
        String s = string;
        for (String[] arg : args) {
            s = s.replace(arg[0], arg[1]);
        }
        return s;
    }

    public static String colorString(String string) {
        return colorString(string, '&');
    }

    public static String colorString(String string, char c) {
        if (string == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        return string.replace(c, '\u00A7').replace("\u00A7\u00A7", String.valueOf(c));
    }

}
