package com.pgms.shared.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CryptoService {

  @Value("${app.crypto.secret}")
  private String secret;

  private static final String ALGO = "AES/GCM/NoPadding";

  public String encrypt(String plainText) {
    try {
      Cipher cipher = Cipher.getInstance(ALGO);
      SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");

      byte[] iv = new byte[12];
      SecureRandom random = new SecureRandom();
      random.nextBytes(iv);

      GCMParameterSpec spec = new GCMParameterSpec(128, iv);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

      byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

      ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
      buffer.put(iv);
      buffer.put(encrypted);

      return Base64.getEncoder().encodeToString(buffer.array());

    } catch (Exception e) {
      throw new RuntimeException("Encryption failed", e);
    }
  }

  public String decrypt(String encryptedText) {
    try {
      byte[] decoded = Base64.getDecoder().decode(encryptedText);
      ByteBuffer buffer = ByteBuffer.wrap(decoded);

      byte[] iv = new byte[12];
      buffer.get(iv);

      byte[] encrypted = new byte[buffer.remaining()];
      buffer.get(encrypted);

      Cipher cipher = Cipher.getInstance(ALGO);
      SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
      GCMParameterSpec spec = new GCMParameterSpec(128, iv);

      cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

      return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);

    } catch (Exception e) {
      throw new RuntimeException("Decryption failed", e);
    }
  }

  public String sha256(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Input cannot be null");
    }

    try {
      String normalized = input.trim().toLowerCase();

      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      byte[] hashBytes = digest.digest(
        normalized.getBytes(StandardCharsets.UTF_8)
      );

      return Base64.getEncoder().encodeToString(hashBytes);

    } catch (Exception e) {
      throw new RuntimeException("Hash failed", e);
    }
  }
}
