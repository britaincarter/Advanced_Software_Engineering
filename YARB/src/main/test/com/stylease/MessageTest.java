package com.stylease;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
//import static org.junit.assert.*;
import junit.framework.*;

public class MessageTest extends TestCase {
  protected Message m;
  protected String chars;
  protected Random r;
  
  protected String msgTxt;
  protected int msgId;
  
  public static final int LONGEST_STR = 100;
  public static final int LONGEST_ID = 1000;
  
  public static String generateString(Random rng, String characters, int length)
  {
    char[] text = new char[length];
    for (int i = 0; i < length; i++)
    {
      text[i] = characters.charAt(rng.nextInt(characters.length()));
    }
    return new String(text);
  }

  @Before
  protected void setUp() {
    chars = "";
    for(int i = 33; i < 126; i++) {
      chars += (char)i;
    }
    
    r = new Random();
    m = new Message();
    
    msgTxt = generateString(r, chars, r.nextInt(LONGEST_STR));
    msgId = r.nextInt(LONGEST_ID);
    m.setId(msgId);
    m.setText(msgTxt);
  }
  
  @Test
  public void testMessage() {
    assertTrue(msgTxt == m.getText());
    assertTrue(msgId == m.getId());
  }
  
  public Message getMessage() {
    return m;
  }
}
