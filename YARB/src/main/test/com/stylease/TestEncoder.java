package com.stylease;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestEncoder {
 
	
	//Test of creation of Message
	//Test Message and Board objects methods. Also tests when a message is placed into a board it is the same object as the original message. 
	@Test(expected=IndexOutOfBoundsException.class)
	public void first(){
			Message m = new Message();
			String s = "Testing";
			
			m.setText(s);
			m.setId(0);
			
			Board b = new Board();
			b.name="Test Board";
			b.id=0;
			
			b.addMessage(m, m.getId());
			
			
			//Message in board is the same object
			assertEquals(m, b.getMessage(m.getId()));
			//Message in board contains same ID as original message
			assertEquals(m.getId(), b.getMessage(m.getId()).getId());		
	}


	@Test(expected=IndexOutOfBoundsException.class)
	public void second(){
	Board b = new Board();
		//Index out of bounds expected
		b.getMessage(1);
	}	
    
    @Test
    //Test adding message to board
    public void third() {
        
        //bl.viewAllMessages(boardId, model)    
        //assertThat(subClass, instanceOf(BaseClass.class));
    }
    	
    	
    	
    	
    	
    
 
}