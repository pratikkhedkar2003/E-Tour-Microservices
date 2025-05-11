package com.pkcoder.authorization_server.security;

import com.nimbusds.jose.jwk.RSAKey;
import com.pkcoder.authorization_server.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Slf4j
@Component
public class KeyUtils {
    private static final String KEY_ALGORITHM = "RSA";
    private static final String KEY_ID = "584b349d-38e8-4a09-b401-635a36b947b1";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${keys.private}")
    private String privateKey;

    @Value("${keys.public}")
    private String publicKey;

    public RSAKey getRSAKeyPair() {
        return generateRSAKeyPair(privateKey, publicKey);
    }

    private RSAKey generateRSAKeyPair(String privateKeyName, String publicKeyName) {
        KeyPair keyPair;
        Path keysDirectory = Paths.get("src", "main", "resources", "keys");
        verifyKeysDirectory(keysDirectory);

        if (Files.exists(keysDirectory.resolve(privateKeyName)) && Files.exists(keysDirectory.resolve(publicKeyName))) {
            log.info("RSA keys already exists. Loading keys from file paths: {}, {}", privateKeyName, publicKeyName);

            try {
                File privateKeyFile = keysDirectory.resolve(privateKeyName).toFile();
                File publicKeyFile = keysDirectory.resolve(publicKeyName).toFile();

                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

                return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(KEY_ID).build();

            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException(exception.getMessage());
            }
        } else {
            if (activeProfile.equalsIgnoreCase("prod")) {
                throw new ApiException("Public and Private key don't exist in prod environment");
            }
            log.info("Generating RSA private key and public keys: {}, {}", privateKeyName, publicKeyName);

            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();

                RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

                try(var fos = new FileOutputStream(keysDirectory.resolve(privateKeyName).toFile())) {
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
                    fos.write(keySpec.getEncoded());
                }

                try(var fos = new FileOutputStream(keysDirectory.resolve(publicKeyName).toFile())) {
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
                    fos.write(keySpec.getEncoded());
                }

                return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(KEY_ID).build();

            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException(exception.getMessage());
            }
        }
    }

    private void verifyKeysDirectory(Path keysDirectory) {
        if (!Files.exists(keysDirectory)) {
            try {
                Files.createDirectories(keysDirectory);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException(exception.getMessage());
            }
            log.info("Keys directory created: {}", keysDirectory);
        }
    }
}
