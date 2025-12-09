package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.security.*;
import io.quarkus.logging.Log;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/crypto")

public class EncryptionResource {

    @ResponseStatus(200)
    @POST
    @Path("/decryptRSA")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String decrypt(EncryptableMessage encryptableMessage) throws NoSuchAlgorithmException {
        String message = encryptableMessage.message;
        try {
            PublicKey pk= ConfigLoader.loadConfig();
            Log.info(pk.getEncoded());
        }catch (Exception e){
            Log.error(e);
        }
        //TODO: decrypt the RSA-encrypted message and return it
        return "decrypted RSA-Message";

    }

    @ResponseStatus(200)
    @POST
    @Path("/encryptRSA")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String encrypt(EncryptableMessage encryptableMessage) throws NoSuchAlgorithmException {
        String message = encryptableMessage.message;
        Log.info(message);
        try {
            PublicKey pk= ConfigLoader.loadConfig();
            Log.info(pk.getEncoded());
        }catch (Exception e){
            Log.error(e);
        }
        //TODO: decrypt the RSA-encrypted message and return it
        return "encrypted RSA-Message";

    }
}


