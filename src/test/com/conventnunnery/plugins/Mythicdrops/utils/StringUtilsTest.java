package com.conventnunnery.plugins.Mythicdrops.utils;

import com.conventnunnery.plugins.mythicdrops.utils.StringUtils;
import junit.framework.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testCapitalize() throws Exception {
        String testString = "pie";

        String string = StringUtils.capitalize(testString);

        Assert.assertNotNull(string);
        Assert.assertEquals("Pie",string);
    }

    @Test
    public void testGetInitCappedString() throws Exception {
        String[] array = new String[]{"I", "like", "pie"};

        String string = StringUtils.getInitCappedString(array);

        Assert.assertNotNull(string);
        Assert.assertEquals("I Like Pie", string);
    }
}
