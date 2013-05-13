/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.Mythicdrops.utils;

public class NumberUtils {

	protected NumberUtils() {

	}

	/**
	 * Gets double from a string with a fallback double.
	 *
	 * @param string   String to check
	 * @param fallBack Fallback double
	 * @return double from string
	 */
	public static double getDouble(String string, double fallBack) {
		double d;
		try {
			d = Double.parseDouble(string);
		} catch (Exception e) {
			d = fallBack;
		}
		return d;
	}

	/**
	 * Gets int from string with a fallback int.
	 *
	 * @param string   String to check
	 * @param fallBack Fallback int
	 * @return int from string
	 */
	public static int getInt(String string, int fallBack) {
		int i;
		try {
			i = (int) Math.round(getDouble(string, (double) fallBack));
		} catch (Exception e) {
			i = fallBack;
		}
		return i;
	}

}
