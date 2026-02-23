package hsm.vertx;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    ConfigStoreOptions propertyWithHierarchical = new ConfigStoreOptions()
      .setFormat("properties")
      .setType("file")
      .setConfig(new JsonObject().put("path", "application.properties").put("hierarchical", true)
      );
    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(propertyWithHierarchical);


    Router router = Router.router(vertx);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    retriever.configStream().handler(c ->{
      c.getJsonObject("server").getString("host");
    });

    Future<JsonObject> future = retriever.getConfig();


    Future<JsonObject> f = retriever.getConfig();
    f.onComplete(ar -> {
      if (ar.failed()) {
        // Failed to retrieve the configuration
      } else {
        JsonObject config = ar.result();
        System.out.println(config);
      }
    });




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
