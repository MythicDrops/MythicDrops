package com.conventnunnery.plugins.Mythicdrops.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class ContainerUtils {

	protected ContainerUtils() {

	}

	/**
	 * Checks if a collection contains a string in any case.
	 *
	 * @param collection the collection
	 * @param string     the string
	 * @return if the collection contains the string
	 */
	public static boolean containsIgnoreCase(Collection<String> collection,
	                                         String string) {
		for (String s : collection) {
			if (s.equalsIgnoreCase(string))
				return true;
		}
		return false;
	}

	/**
	 * Takes a list of Strings and returns it with no duplicates
	 *
	 * @param strings List to suppress
	 * @return list of Strings with no duplicates
	 */
	public static List<String> suppressDuplicates(List<String> strings) {
		return new ArrayList<String>(new LinkedHashSet<String>(strings));
	}

	/**
	 * Takes a collection of Strings and returns it as an array
	 * @param collection Collection of Strings
	 * @return array of strings
	 */
	public static String[] toStringArray(Collection<String> collection) {
		int size = collection.size();
		return collection.toArray(new String[size]);
	}

}
