package org.acme;
import io.quarkus.logging.Log;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Enumeration;


public class ConfigLoader {
    public static PublicKey loadConfig() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {

        String configuration = "pkcs11.cfg";

        Provider sunPkcs11 = Security.getProvider("SunPKCS11");
        Provider pkcsImplementation = sunPkcs11.configure(configuration);

        if (pkcsImplementation != null) {
            Log.info("PKCSImplementation not null");
        }

        Security.addProvider(pkcsImplementation);
        
        KeyStore hsmKeyStore = KeyStore.getInstance("PKCS11", pkcsImplementation);
        String userPin = "98765432"; // The pin to unlock the HSM-TOKEN

        hsmKeyStore.load(null, userPin.toCharArray());
        String keyID = "2222"; // The key identifier or alias
        String keyPin = ""; // Optional pin to unlock the key => not needed

        //this is null for some reason
        Key k= hsmKeyStore.getKey(keyID, userPin.toCharArray());

        Log.info("Key is: "+ k);



        PublicKey publicKey= (PublicKey) hsmKeyStore.getKey(keyID, null);

        return publicKey;


    }

}

