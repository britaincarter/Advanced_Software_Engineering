package com.stylease.entities;

public abstract class IdItem<IdType> {

  private IdType id;
  
  public IdType getId() {
    return this.id;
  }
  
  public void setId(IdType id) {
    this.id = id;
  }
}
