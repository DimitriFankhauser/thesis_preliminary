package org.acme;

import javax.crypto.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RsaUtil {
    private Logger Log = LoggerFactory.getLogger(RsaUtil.class);
    private String configFilePath;
    private String userPin;
    private String keyID;
    private String keyAlias;
    private KeyStore hsmKeyStore;

    public RsaUtil() {
        this.configFilePath = "pkcs11.cfg";
        this.userPin = "123456789"; // The pin to unlock the HSM-TOKEN
        this.keyID = "7777"; // The key identifier or alias
        this.keyAlias = "rsaGenesis";
        setup();
    }

    public RsaUtil(String configFilePath, String userPin, String keyID, String keyAlias) {
        this.configFilePath = configFilePath;
        this.userPin = userPin;
        this.keyID = keyID;
        this.keyAlias = keyAlias;
        setup();
    }

    private Provider ensurePKCSImplementation(String configfilePath) {
        Provider sunPkcs11 = Security.getProvider("SunPKCS11");
        Provider pkcsImplementation = sunPkcs11.configure(configfilePath);
        if (pkcsImplementation != null) {
            Log.info("PKCS implementation is not null");
            Security.addProvider(pkcsImplementation);
            return pkcsImplementation;
        }
        throw new RuntimeException("No PKCS Implementation found");
    }

    public String encrypt(String message) {

        Cipher encryptCipher = null;
        try {
            RSAPublicKey publicKey = (RSAPublicKey) hsmKeyStore.getCertificate(this.keyAlias).getPublicKey();
            encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);

            return Base64.getEncoder().encodeToString(encryptedMessageBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | KeyStoreException e) {
            this.Log.error(e.getClass() + e.getMessage());
            return "";
        }
    }

    public String decrypt(String ciphertext) {

        Cipher decryptCipher = null;
        try {
            PrivateKey privateKey = (PrivateKey) hsmKeyStore.getKey(this.keyAlias, userPin.toCharArray());
            decryptCipher = Cipher.getInstance("RSA", this.hsmKeyStore.getProvider());
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            // decode from string back to byte array
            byte[] secretMessageBytes = ciphertext.getBytes(StandardCharsets.UTF_8);

            byte[] encryptedMessageBytes = Base64.getDecoder().decode(secretMessageBytes);

            byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
            String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
            return decryptedMessage;

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
                | UnrecoverableKeyException | KeyStoreException | InvalidKeyException e) {
            this.Log.error(e.getClass() + e.getMessage());
            return "";
        }
    }

    private void setup() {
        this.Log = LoggerFactory.getLogger(RsaUtil.class);
        try {
            Provider pkcsImplementation = ensurePKCSImplementation(this.configFilePath);
            this.hsmKeyStore = KeyStore.getInstance("PKCS11", pkcsImplementation);

            // load keystore and log in
            this.hsmKeyStore.load(null, userPin.toCharArray());

        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
            Log.error(e.getClass() + e.getMessage());
        }

    }
}
