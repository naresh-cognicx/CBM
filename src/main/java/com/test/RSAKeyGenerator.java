package com.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class RSAKeyGenerator {

	public static void main(String[] args) {
		try {
			PublicKey publickey=loadPublicKeyFromFile("public_key");
			String encrypted=encrypt("welcome", publickey);
			System.out.println("Encrypted SOW :"+encrypted);
			PrivateKey privateKey=loadPrivateKeyFromFile("private_key");
			System.out.println(decrypt(encrypted, privateKey));
			
			  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			  String encodedPass=passwordEncoder.encode("welcome");
			  System.out.println("Encoded Pass:"+encodedPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void generateRSAKeyPair() {
        try {
            // Generate an RSA key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Key size of 2048 bits
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get the public and private keys from the key pair
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Store the keys in separate files
            storeKeyToFile(publicKey, "public_key");
            storeKeyToFile(privateKey, "private_key");
            System.out.println("Keys generated and stored successfully.");
        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println("Error generating/storing RSA key pair: " + e.getMessage());
        }
	}
	
    private static void storeKeyToFile(java.security.Key key, String fileName) throws IOException {
        byte[] keyBytes = key.getEncoded();
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(keyBytes);
        }
    }
    
    
    private static PublicKey loadPublicKeyFromFile(String fileName) throws Exception {
        // Read the public key bytes from the file
        byte[] keyBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(fileName));

        // Convert the bytes to a PublicKey object
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    
    private static String encrypt(String data, PublicKey publicKey) throws Exception {
        // Create a Cipher object and initialize it for encryption using the public key
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Encrypt the data
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        // Convert the encrypted bytes to a Base64 encoded string
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    
    
    private static PrivateKey loadPrivateKeyFromFile(String fileName) throws Exception {
        // Read the private key bytes from the file
        byte[] keyBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(fileName));

        // Convert the bytes to a PrivateKey object
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
    
    private static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
        // Decode the Base64 encoded encrypted data
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // Create a Cipher object and initialize it for decryption using the private key
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Decrypt the data
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert the decrypted bytes to a string
        return new String(decryptedBytes);
    }

    
	
}
