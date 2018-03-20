package com.gouttebessisg;

import java.util.LinkedHashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MyFirstVerticle extends AbstractVerticle {

  private Map<Integer, Car> cars = new LinkedHashMap<>();

  @Override
  public void start(Future<Void> fut) {

    createSomeData();

   // Create a router object.
   Router router = Router.router(vertx);
  
   // Bind "/" to our hello message - so we are still compatible.
   router.route("/").handler(routingContext -> {
     HttpServerResponse response = routingContext.response();
     response
         .putHeader("content-type", "text/html")
         .end("<h1>Hello from my first Vert.x 3 application</h1>");
   });

    router.get("/api/cars").handler(this::getAll);
    router.route("/api/cars*").handler(BodyHandler.create());
    router.post("/api/cars").handler(this::addOne);
    router.delete("/api/cars/:id").handler(this::deleteOne);

   // Create the HTTP server and pass the "accept" method to the request handler.
   vertx
       .createHttpServer()
       .requestHandler(router::accept)
       .listen(
           // Retrieve the port from the configuration,
           // default to 8080.
           config().getInteger("http.port", 8080),
           result -> {
             if (result.succeeded()) {
               fut.complete();
             } else {
               fut.fail(result.cause());
             }
           }
       );
  }

  private void getAll(RoutingContext routingContext) {
    routingContext.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(Json.encodePrettily(cars.values()));
  }
  
  private void createSomeData() {
    Car car1 = new Car("Renault Clio", "Bleu");
    cars.put(car1.getId(), car1);
    Car car2 = new Car("Peugeot 207", "Rouge");
    cars.put(car2.getId(), car2);
  }

  private void addOne(RoutingContext routingContext) {
    final Car car = Json.decodeValue(routingContext.getBodyAsString(),
    Car.class);
    cars.put(car.getId(), car);
    routingContext.response()
        .setStatusCode(201)
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(Json.encodePrettily(car));
  }

  private void deleteOne(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      Integer idAsInteger = Integer.valueOf(id);
      cars.remove(idAsInteger);
    }
    routingContext.response().setStatusCode(204).end();
  }
}