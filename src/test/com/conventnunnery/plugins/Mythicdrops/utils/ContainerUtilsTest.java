package com.conventnunnery.plugins.Mythicdrops.utils;

import com.conventnunnery.plugins.mythicdrops.utils.ContainerUtils;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ContainerUtilsTest {

	@Test
	public void testContainsIgnoreCase() throws Exception {
		List<String> l = new ArrayList<String>();
		l.add("one");
		l.add("two");
		l.add("three");
		l.add("four");

		boolean b = ContainerUtils.containsIgnoreCase(l, "THREE");
		Assert.assertEquals(true, b);
	}

	@Test
	public void testSuppressDuplicates() throws Exception {
		List<String> l = new ArrayList<String>();
		l.add("one");
		l.add("two");
		l.add("two");
		l.add("three");
		l.add("two");
		l.add("four");

		List<String> noDupes = ContainerUtils.suppressDuplicates(l);
		Assert.assertNotNull(noDupes);
		Assert.assertEquals(4, noDupes.size());
		Assert.assertEquals("one", noDupes.get(0));
		Assert.assertEquals("two", noDupes.get(1));
		Assert.assertEquals("three", noDupes.get(2));
		Assert.assertEquals("four", noDupes.get(3));
	}

	@Test
	public void testToStringArray() throws Exception {
		List<String> l = new ArrayList<String>();
		l.add("one");
		l.add("two");
		l.add("three");
		l.add("four");

		String[] array = ContainerUtils.toStringArray(l);
		Assert.assertNotNull(array);
		Assert.assertEquals(4, array.length);
	}
}
