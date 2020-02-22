/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringListUtilTest {

  @Test
  public void removeIfMatchesNegative() throws Exception {
    List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
    List<String> list2 = Arrays.asList("Like", "Butts", "I");

    List<String> actual = StringListUtil.removeIfMatches(list1, list2);

    Assertions.assertEquals(list1, actual);
  }

  @Test
  public void removeIfMatchesPositive() throws Exception {
    List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
    List<String> list2 = Arrays.asList("Big", "Butts", "and");

    List<String> expected = Arrays.asList("I", "Like", "I", "Cannot", "Lie");
    List<String> actual = StringListUtil.removeIfMatches(list1, list2);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void removeIfMatchesEmptyArg1() throws Exception {
    List<String> list1 = new ArrayList<>();
    List<String> list2 = Arrays.asList("Big", "Butts", "and");

    List<String> actual = StringListUtil.removeIfMatches(list1, list2);

    Assertions.assertEquals(list1, actual);
  }

  @Test
  public void removeIfMatchesEmptyArg2() throws Exception {
    List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
    List<String> list2 = new ArrayList<>();

    List<String> actual = StringListUtil.removeIfMatches(list1, list2);

    Assertions.assertEquals(list1, actual);
  }

  @Test
  public void removeIfMatchesColorlessNegative() throws Exception {
    List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
    List<String> list2 = Arrays.asList("Like", "Butts", "I");

    List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

    Assertions.assertEquals(list1, actual);
  }

  @Test
  public void removeIfMatchesColorlessPositive() throws Exception {
    List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
    List<String> list2 = Arrays.asList("Big", "Butts", "and");

    List<String> expected = Arrays.asList("I", "Like", "I", "Cannot", "Lie");
    List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void removeIfMatchesColorlessEmptyArg1() throws Exception {
    List<String> list1 = new ArrayList<>();
    List<String> list2 = Arrays.asList("Big", "Butts", "and");

    List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

    Assertions.assertEquals(list1, actual);
  }

  @Test
  public void removeIfMatchesColorlessEmptyArg2() throws Exception {
    List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
    List<String> list2 = new ArrayList<>();

    List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

    Assertions.assertEquals(list1, actual);
  }
}
