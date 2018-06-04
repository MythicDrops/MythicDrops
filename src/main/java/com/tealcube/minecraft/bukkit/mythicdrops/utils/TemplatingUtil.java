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

import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.templating.OpString;
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandRomanTemplate;
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandSignTemplate;
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandTemplate;
import com.tealcube.minecraft.bukkit.mythicdrops.templating.Template;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public final class TemplatingUtil {

  private static final Logger LOGGER = MythicLoggerFactory.getLogger(TemplatingUtil.class);
  private static final Pattern PERCENTAGE_PATTERN = Pattern.compile("%(?s)(.*?)%");

  private static final Template RAND_INTEGER_RANGE = new RandTemplate();
  private static final Template RANDSIGN = new RandSignTemplate();
  private static final Template RAND_ROMAN_RANGE = new RandRomanTemplate();

  static OpString opsString(String str) {
    String[] opString = StringUtils.trimToEmpty(str).split("\\s+", 2);
    String operation = opString.length > 0 ? opString[0] : "";
    String args = opString.length > 1 ? opString[1] : "";
    return new OpString(operation, args);
  }

  public static String template(String string) {
    String retString = string;
    Matcher m = PERCENTAGE_PATTERN.matcher(string);
    while (m.find()) {
      String check = m.group();
      String checkWithoutPercentages = check.replace("%", "");
      OpString opString = opsString(checkWithoutPercentages);
      LOGGER.fine("opString=\"" + opString + "\"");
      if (RAND_INTEGER_RANGE.test(opString.getOperation())) {
        LOGGER.fine("Templating using RAND_INTEGER_RANGE");
        retString = StringUtils.replace(retString, check, RAND_INTEGER_RANGE.apply(opString.getArguments()));
        continue;
      }
      if (RANDSIGN.test(opString.getOperation())) {
        LOGGER.fine("Templating using RANDSIGN");
        retString = StringUtils.replace(retString, check, RANDSIGN.apply(opString.getArguments()));
        continue;
      }
      if (RAND_ROMAN_RANGE.test(opString.getOperation())) {
        LOGGER.fine("Templating using RAND_ROMAN_RANGE");
        retString = StringUtils.replace(retString, check, RAND_ROMAN_RANGE.apply(opString.getArguments()));
      }
    }
    return retString;
  }

}
