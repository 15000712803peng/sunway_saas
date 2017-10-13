package com.cnsunway.saas.wash.framework.utils;

public class PhoneCheck {

	public static boolean check(String phone) {

		return phone.matches("1[03456789]\\d{9}");
	}
}
