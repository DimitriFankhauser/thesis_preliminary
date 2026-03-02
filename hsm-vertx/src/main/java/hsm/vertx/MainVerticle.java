package hsm.vertx;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends VerticleBase {

  @Override
  public Future<?> start() throws Exception {

    Router router = Router.router(vertx);

    ConfigStoreOptions configStoreOptions = new ConfigStoreOptions().setType("file").setFormat("json").setConfig(new JsonObject().put("path", "config.json"));
    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(configStoreOptions));

    router.route().handler(BodyHandler.create());

    Future<RsaUtil> rsaUtilFuture = retriever.getConfig().map(config -> new RsaUtil(config.getString("userpin"), config.getString("keyAlias")));

    router.get("/ping").handler(rc -> {
      retriever.getConfig().onSuccess(config -> {
        String message = config.getString("message");
        rc.request().response().putHeader("content-type", "text/plain").end(message);
      }).onFailure(err -> rc.fail(500));
    })


    ;
    router.post("/crypto/encryptRSA").handler(rc -> {
      EncryptableMessage e = rc.body().asJsonObject().mapTo(EncryptableMessage.class);

      // if the future was successfully completed, use the returned RsaUtil object and encrypt with it
      rsaUtilFuture.onSuccess(util -> {
        String ciphertext = util.encrypt(e.message);
        rc.response().putHeader("content-type", "text/plain").end(ciphertext);
      });
    });

    router.post("/crypto/decryptRSA").handler(rc -> {
      EncryptableMessage e = rc.body().asJsonObject().mapTo(EncryptableMessage.class);

      rsaUtilFuture.onSuccess(util -> {
        String cleartext = util.decrypt(e.message);
        rc.response().putHeader("content-type", "text/plain").end("Cleartext:  " + cleartext);
      });

    });


    return retriever.getConfig().compose(cfg -> {
      return vertx.createHttpServer().requestHandler(router).listen(cfg.getInteger("port"));
    });

  }
}
