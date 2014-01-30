package net.nunnerycode.bukkit.mythicdrops.utils;

import java.util.ArrayList;
import java.util.List;

public final class StringUtil {

  private StringUtil() {
    // do nothing
  }

  public static String replaceArgs(String string, String[][] args) {
    String s = string;
    for (String[] arg : args) {
      s = s.replace(arg[0], arg[1]);
    }
    return s;
  }

  public static List<String> replaceArgs(List<String> strings, String[][] args) {
    List<String> list = new ArrayList<>();
    for (String s : strings) {
      list.add(replaceArgs(s, args));
    }
    return list;
  }

}
