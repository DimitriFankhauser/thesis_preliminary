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
    @Path("/encryptRSA")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String encrypt(EncryptableMessage encryptableMessage) throws NoSuchAlgorithmException {
        String message = encryptableMessage.message;
        Log.info(message);
        try {
            RsaUtil rsaUtil= new RsaUtil();
            String ciphertext=rsaUtil.encrypt(encryptableMessage.message);
            Log.info(ciphertext);
            return ciphertext;
        }catch (Exception e){
            Log.error(e);
        }
        //TODO: decrypt the RSA-encrypted message and return it
        return "";
    }
}


