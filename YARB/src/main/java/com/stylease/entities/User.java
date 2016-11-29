package com.stylease.entities;

import java.util.ArrayList;
import java.util.List;

import com.stormpath.sdk.account.Account;

public class User extends IdItem<Long> {

  private String name;
  private Account account;
  
  private List<Key> keys;
  
  //public User(String name)
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Account getAccount() {
    return this.account;
  }
  
  public void setAccount(Account acct) {
    this.name = acct.getUsername();
    this.account = acct;
  }
  
  public List<Key> getKeys() {
    return this.keys;
  }
  
  public void setKeys(List<Key> keys) {
    this.keys = keys;
  }
  
  public void addKey(Key k) {
    this.keys.add(k);
  }
  
}
