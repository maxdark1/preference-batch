package ca.homedepot.preference.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.experimental.UtilityClass;


@UtilityClass
public final class DecodeUtil
{

	public static String convertToHex(String emailId) throws NoSuchAlgorithmException
	{
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		String text = emailId.toLowerCase();
		// Change this to UTF-16 if needed
		messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
		byte[] digest = messageDigest.digest();
		return String.format("%064X", new BigInteger(1, digest));
	}
}
