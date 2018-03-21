package com.gouttebessisg;

import io.vertx.core.json.JsonObject;

public class Car {

  private String id;

  private String name;

  private String color;

  public Car(String name, String color) {
    this.name = name;
    this.color = color;
    this.id = "";
  }

  public Car(JsonObject json) {
    this.name = json.getString("name");
    this.color = json.getString("color");
    this.id = json.getString("_id");
  }

  public Car() {
    this.id = "";
  }

  public Car(String id, String name, String color) {
    this.id = id;
    this.name = name;
    this.color = color;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject()
        .put("name", name)
        .put("color", color);
    if (id != null && !id.isEmpty()) {
      json.put("_id", id);
    }
    return json;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public String getId() {
    return id;
  }

  public Car setName(String name) {
    this.name = name;
    return this;
  }

  public Car setColor(String color) {
    this.color = color;
    return this;
  }

  public Car setId(String id) {
    this.id = id;
    return this;
  }
}