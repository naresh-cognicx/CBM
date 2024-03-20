package com.test;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import com.cognicx.AppointmentRemainder.util.Encryption;

public class Test {

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
	
	
	public static void main(String[] args) {
		System.out.println(loadPrivateKeyFromFile("test"));
	}
}
