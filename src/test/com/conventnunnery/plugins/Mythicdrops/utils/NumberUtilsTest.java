package com.conventnunnery.plugins.Mythicdrops.utils;

import com.conventnunnery.plugins.mythicdrops.utils.NumberUtils;
import junit.framework.Assert;
import org.junit.Test;

public class NumberUtilsTest {
	@Test
	public void testGetDouble() throws Exception {
		String value = "30.0";

		double d = NumberUtils.getDouble(value, 0.0);

		Assert.assertEquals(30.0, d);
	}

	@Test
	public void testGetInt() throws Exception {
		String value = "30";

		int i = NumberUtils.getInt(value, 30);

		Assert.assertEquals(30, i);
	}
}
