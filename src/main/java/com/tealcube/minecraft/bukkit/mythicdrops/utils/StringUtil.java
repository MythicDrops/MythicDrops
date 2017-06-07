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
package com.tealcube.minecraft.bukkit.mythicdrops.utils;

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
