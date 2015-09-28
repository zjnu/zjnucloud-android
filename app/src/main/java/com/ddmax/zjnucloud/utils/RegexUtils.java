package com.ddmax.zjnucloud.utils;

import java.util.regex.Pattern;

/**
 * @author ddMax
 * @since 2015/9/28 13:06.
 */
public class RegexUtils {

	public static final String EMAIL = "[\\w\\-]+@[\\w\\-]+(\\.[\\w\\-]+)*(\\.[a-zA-Z]+)";

	public static boolean matchEmail(String email) {
		if (Pattern.matches(EMAIL, email)) {
			return true;
		}
		return false;
	}
}
