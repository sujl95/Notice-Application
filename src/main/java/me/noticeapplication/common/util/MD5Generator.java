package me.noticeapplication.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.ToString;

@ToString
public class MD5Generator {

	private final String result;

	public MD5Generator(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(input.getBytes(StandardCharsets.UTF_8));
		byte[] hash = md.digest();
		StringBuilder hexHash = new StringBuilder();
		for (byte b : hash) {
			String hexString = String.format("%02x", b);
			hexHash.append(hexString);
		}
		result = hexHash.toString();
	}

}
