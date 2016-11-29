package com.stylease.entities;

public class Key extends IdItem<Long> {

  public static int CAN_READ = 0;
  public static int CAN_WRITE = 1;
  public static int INVITE_USERS = 2;
  public static int ADMINISTER = 3;
  
  private boolean[] perms = {false, false, false, false};
  private String name;
  
  public boolean getPermission(int perm) {
    if(perm > perms.length) {
      return false;
    }
    
    return perms[perm];
  }
  
  public void setPermission(int perm, boolean value) {
    perms[perm] = value;
  }
  
  public boolean canRead() {
    return getPermission(CAN_READ);
  }
  
  public boolean canWrite() {
    return getPermission(CAN_WRITE);
  }
  
  public boolean canInvite() {
    return getPermission(INVITE_USERS);
  }
  
  public boolean isAdmin() {
    return getPermission(ADMINISTER);
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
