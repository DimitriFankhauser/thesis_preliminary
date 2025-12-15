package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    public Response encrypt(EncryptableMessage encryptableMessage) throws NoSuchAlgorithmException {
        Log.info(encryptableMessage.message);
        try {
            RsaUtil rsaUtil = new RsaUtil();
            String ciphertext = rsaUtil.encrypt(encryptableMessage.message);
            Log.info(ciphertext);
            return Response.ok(ciphertext).build();
        } catch (Exception e) {
            Log.error(e);
            return Response.serverError().build();

        }
    }

    @ResponseStatus(200)
    @POST
    @Path("/decryptRSA")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response decrypt(EncryptableMessage ciphertext) throws NoSuchAlgorithmException {
        try {
            Log.info("received ciphertext" + ciphertext.message);
            RsaUtil rsaUtil = new RsaUtil();
            String message = rsaUtil.decrypt(ciphertext.message);
            Log.info(message);
            return Response.ok(message).build();
        } catch (Exception e) {
            Log.error(e);
            return Response.serverError().build();
        }
    }
}
