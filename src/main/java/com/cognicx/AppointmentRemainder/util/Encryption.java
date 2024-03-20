package com.cognicx.AppointmentRemainder.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.poi.util.IOUtils;

public class Encryption {

	public byte[] get() {
		byte[] bytes = null;
		InputStream i = getClass().getResourceAsStream("/key_pvt");
		try {
			bytes = IOUtils.toByteArray(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}

	public static PrivateKey loadPrivateKeyFromFile(String keyFile) {

		PrivateKey privateKey = null;
		// Path path = Paths.get(keyFile);
		byte[] bytes;
		try {
			Encryption e = new Encryption();

			// File resource = new ClassPathResource("key_pvt").getFile();
			bytes = e.get();
			PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
			KeyFactory kf;
			kf = KeyFactory.getInstance("RSA");

			privateKey = kf.generatePrivate(ks);

//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return privateKey;

	}

	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(cipherText);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), StandardCharsets.UTF_8);
	}

}
