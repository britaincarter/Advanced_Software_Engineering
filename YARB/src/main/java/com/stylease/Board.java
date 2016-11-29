package com.stylease;

import java.util.*;

import com.stylease.entities.Message;

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
      m.setId(i);
      this.messages.add(i, m);
      System.out.println("Added message " + m.getId());
    }

    public Message getMessage(int i) {
      // TODO Auto-generated method stub
      return messages.get(i);
    }

}
