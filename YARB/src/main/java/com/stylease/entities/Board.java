package com.stylease.entities;

import java.util.Date;
import java.util.List;

public class Board extends IdItem<Long> {

  private String name;
  private Date created;
  private boolean enabled;
  private List<Message> messages;
  private List<Key> keys;
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Date getCreated() {
    return this.created;
  }
  
  public void setCreated(Date created) {
    this.created = created;
  }
  
  public void setCreated(java.sql.Date created) {
    this.created = new Date(created.getTime());
  }
  
  public boolean getEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public List<Message> getMessages() {
    return this.messages;
  }
  
  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }
  
  public List<Key> getKeys() {
    return this.keys;
  }
  
  public void setKeys(List<Key> keys) {
    this.keys = keys;
  }
  
  public void addMessage(Message m) {
    messages.add(m);
  }
  
  public void addMessage(Message m, int i) {
    messages.add(i, m);
  }
  
  public void addKey(Key k) {
    keys.add(k);
  }
}
