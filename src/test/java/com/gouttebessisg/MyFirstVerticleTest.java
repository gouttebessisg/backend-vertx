package com.gouttebessisg;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MyFirstVerticleTest {

  private Vertx vertx;
  private int port;

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    ServerSocket socket;
	try {
		socket = new ServerSocket(0);
    port = socket.getLocalPort();
    socket.close();
  } catch (IOException e1) {
    port = 8081;
	}

  DeploymentOptions options = new DeploymentOptions()
    .setConfig(new JsonObject().put("http.port", port)
    );
     vertx.deployVerticle(MyFirstVerticle.class.getName(), options, context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testMyApplication(TestContext context) {
    final Async async = context.async();

    vertx.createHttpClient().getNow(port, "localhost", "/",
     response -> {
      response.handler(body -> {
        context.assertTrue(body.toString().contains("Hello"));
        async.complete();
      });
    });
  }

  @Test
public void checkThatWeCanAdd(TestContext context) {
  Async async = context.async();
  final String json = Json.encodePrettily(new Car("Car1", "White"));
  final String length = Integer.toString(json.length());
  vertx.createHttpClient().post(port, "localhost", "/api/cars")
      .putHeader("content-type", "application/json")
      .putHeader("content-length", length)
      .handler(response -> {
        context.assertEquals(response.statusCode(), 201);
        context.assertTrue(response.headers().get("content-type").contains("application/json"));
        response.bodyHandler(body -> {
          final Car car = Json.decodeValue(body.toString(), Car.class);
          context.assertEquals(car.getName(), "Car1");
          context.assertEquals(car.getColor(), "White");
          context.assertNotNull(car.getId());
          async.complete();
        });
      })
      .write(json)
      .end();
}
}