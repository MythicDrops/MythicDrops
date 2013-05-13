package com.conventnunnery.plugins.mythicdrops.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {
    protected StringUtils() {

    }

    public static String getInitCappedString(String... args) {
        List<String> strings = Arrays.asList(args);
        List<String> initCappedList = new ArrayList<String>();
        for (String s : strings) {
            initCappedList.add(capitalize(s));
        }
        return initCappedList.toString().replace("[", "").replace("]", "").replace(",", "");
    }

    public static String capitalize(String param) {
        if(param != null && param.length()>0){
            char[] charArray = param.toCharArray(); // convert into char array
            charArray[0] = Character.toUpperCase(charArray[0]); // set capital letter to first postion
            return new String(charArray); // return desired output
        }else{
            return "";
        }
    }
}
