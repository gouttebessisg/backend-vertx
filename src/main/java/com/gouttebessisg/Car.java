package com.gouttebessisg;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {

  private static final AtomicInteger COUNTER = new AtomicInteger();

  private final int id;

  private String name;

  private String color;

  public Car(String name, String color) {
    this.id = COUNTER.getAndIncrement();
    this.name = name;
    this.color = color;
  }

  public Car() {
    this.id = COUNTER.getAndIncrement();
  }

  public int getId() {
      return this.id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }
}