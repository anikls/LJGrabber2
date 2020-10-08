package com.grabber.ljgrabber.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Slf4j
public class Conversion {
	
	private static final String REGEX_BASE_64 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
	private static final Pattern PATTERN_BASE64 = Pattern.compile(REGEX_BASE_64);
	
	public static byte[] base64ToBytes(String base64) {
		return DatatypeConverter.parseBase64Binary(base64); 
	}

	public static String base64ToString(String base64, String codepage) {
		String value = "";
		try {
			byte[] b = base64ToBytes(base64);
			value = new String(b, codepage);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return value;
	}

	public static String bytesToBase64(byte[] b) {
		return DatatypeConverter.printBase64Binary(b); 
	}

	public static boolean isBase64(String str) {
		Matcher matcher = PATTERN_BASE64.matcher(str);
		return matcher.matches();
	}
	
}
