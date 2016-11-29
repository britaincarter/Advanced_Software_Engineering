package com.stylease;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class BoardTest extends TestCase {

  protected Board b;
  protected ArrayList<Message> messages;
  
  public static final int MSG_COUNT = 100;
  public static final int MAX_ID = 1000;
  public static final int LONGEST_STR = 10000;
  public static final int LONGEST_ID = 1000;
  
  @Before
  public void setUp() {
    b = new Board();
    messages = new ArrayList<>(MSG_COUNT);
    
    String chars = "";
    for(int i = 33; i < 126; i++) {
      chars += (char)i;
    }
    
    Random r = new Random();
    
    for(int i = 0; i < MSG_COUNT; i++) {
      
      Message mtest = new Message();
      mtest.setId(i);
      mtest.setText(MessageTest.generateString(r, chars, LONGEST_STR));
      
      b.addMessage(mtest, i);
      messages.add(mtest);
    }
  }
  
  @Test
  public void testBoard() {
    for(int i = 0; i < MSG_COUNT; i++) {
      
      Message am = messages.get(i);
      Message bm = b.messages.get(am.getId());
      
      assertTrue(am.getText() == bm.getText());
      assertTrue(am.getId() == bm.getId());
      assertTrue(am == bm);
    }
  }
  
}
