package com.stylease;

import java.util.*;

public class Board {
    public ArrayList<Message> messages;
    String name;
    
    int id;
    
    public Board() {
        messages = new ArrayList<>();
    }
    
    public String getName() {
      return name;
    }
   
    public int getId() {
      return id;
    }
  
    public void setId(int id) {
        this.id = id;
    }
/*
    public ArrayList<Message> stringListToMessageList() {
        ArrayList<Message> messageList = new ArrayList<Message>(this.messages.size());
        for(int i = 0; i < this.messages.size(); i++) {
            messageList.add(new Message(i, this.messages.get(i)));
        }
        return messageList;
    }
*/    
    public void addMessage(Message m, int i) {
      m.id = i;
      this.messages.add(i, m);
      System.out.println("Added message " + m.id);
    }

}
