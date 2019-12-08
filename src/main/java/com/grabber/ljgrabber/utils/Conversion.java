package com.grabber.ljgrabber.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Conversion {
	
	private final static String regexBase64 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
	private final static Pattern PATTERN_BASE64 = Pattern.compile(regexBase64);
	
	public static byte[] base64ToBytes(String base64) {
		return DatatypeConverter.parseBase64Binary(base64); 
	}

	public static String base64ToString(String base64, String codepage) {
		String value = "";
		try {
			byte[] b = base64ToBytes(base64);
			value = new String(b, codepage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
