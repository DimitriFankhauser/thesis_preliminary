package hsm.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    RsaUtil rsaUtil= new RsaUtil();

    router.post("/crypto/encryptRSA").handler(rc -> {
      EncryptableMessage e= rc.body().asJsonObject().mapTo(EncryptableMessage.class);
      String ciphertext = rsaUtil.encrypt(e.message);
      rc.response().putHeader("content-type","text/plain").end(ciphertext);
    });

    router.post("/crypto/decryptRSA").handler(rc -> {
      EncryptableMessage e= rc.body().asJsonObject().mapTo(EncryptableMessage.class);
      String cleartext = rsaUtil.decrypt(e.message);
      rc.response().putHeader("content-type","text/plain").end("Cleartext:  "+cleartext);
    });

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080)
      .onSuccess(server -> startPromise.complete())
      .onFailure(startPromise::fail);
  }
}
