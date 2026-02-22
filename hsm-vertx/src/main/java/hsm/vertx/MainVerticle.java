package hsm.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.get("/question").handler(ctx -> {
      ctx.response()
        .putHeader("content-type", "text/plain")
        .end("What is your favourite food?");
    });

    router.post("/question").handler(ctx -> {
      ctx.response()
        .putHeader("content-type", "text/plain")
        .end("You just sent a POST Request");
    });

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080);
  }
}
