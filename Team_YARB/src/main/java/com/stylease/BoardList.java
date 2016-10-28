package com.stylease;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BoardList {

  private static int STATIC_BOARD_COUNT = 20;
  private static String[][] STATIC_MESSAGES = {
    {
      "It sifts from Leaden Sieves -",
      "It powders all the Wood.",
      "It fills with Alabaster Wool",
      "The Wrinkles of the Road -",
    },
    {
      "It makes an even Face",
      "Of Mountain, and of Plain -",
      "Unbroken Forehead from the East",
      "Unto the East again -",
    },
    {
      "It reaches to the Fence -",
      "It wraps it Rail by Rail",
      "Till it is lost in Fleeces -",
      "It deals Celestial Vail",
    },
    {
      "To Stump, and Stack - and Stem -",
      "A Summer's empty Room -",
      "Acres of Joints, where Harvests were,",
      "Recordless, but for them -",
    },
    {
      "It Ruffles Wrists of Posts",
      "As Ankles of a Queen -",
      "Then stills it's Artisans - like Ghosts -",
      "Denying they have been -",
    },
    {
      "THESE are the days when birds come back,",
      "A very few, a bird or two,",
      "To take a backward look.",
    },
    {
      "These are the days when skies put on",
      "The old, old sophistries of June,--",
      "A blue and gold mistake.",
    },
    {
      "Oh, fraud that cannot cheat the bee,",
      "Almost thy plausibility",
      "Induces my belief,",
    },
    {
      "Till ranks of seeds their witness bear,",
      "And softly through the altered air",
      "Hurries a timid leaf!",
    },
    {
      "Oh, sacrament of summer days,",
      "Oh, last communion in the haze,",
      "Permit a child to join,",
    },
    {
      "Thy sacred emblems to partake,",
      "Thy consecrated bread to break,",
      "Taste thine immortal wine!",
    }
  };
  
  private ArrayList<Board> boards;
  
  private ArrayList<Board> getBoards() {
    ArrayList<Board> boardList = new ArrayList<>();
    for(int i = 0; i < STATIC_BOARD_COUNT; i++) {
      int messagesIdx = i % STATIC_MESSAGES.length;
      Board b = new Board();
      b.name = STATIC_MESSAGES[messagesIdx][0];
      b.id = i;
      for(int j = 0; j < STATIC_MESSAGES[messagesIdx].length; j++) {
        Message m = new Message();
        
        m.setText(STATIC_MESSAGES[messagesIdx][j]);
        b.addMessage(m, j);
      }
      
      boardList.add(i, b);
    }
    
    return boardList;
  }
  
  public BoardList() {
    boards = getBoards();
  }
  
  @GetMapping("/b_list")
  public String showBoards(ModelMap model) {
    model.addAttribute("boards", boards);
    return "b_list";
  }
  
  @GetMapping("/m_list/{boardId}")
  public String viewAllMessages(@PathVariable int boardId, ModelMap model) {
    if(boardId >= boards.size()) {
      throw new ResourceNotFoundException();
    }
    
    for(int i = 0; i < boards.get(boardId).messages.size(); i++) {
      System.out.println(boards.get(boardId).id);
    }
    
    model.addAttribute("allMessages", boards.get(boardId).messages);
    model.addAttribute("board", boardId);
    return "m_list";
  }
  
  @GetMapping("/")
  public String welcomeHome(HttpServletRequest req, ModelMap model) {
      Account account = AccountResolver.INSTANCE.getAccount(req);
      if (account != null) {
          model.addAttribute(account);
      }
    
    //return "home";
      return showBoards(model) ;
  }

  @GetMapping("/m/{boardId}/{messageId}")
  public String viewMessage(@PathVariable int boardId, @PathVariable int messageId, ModelMap model) {
    
    String post = "No message with that ID.";
    if(this.boards.size() > boardId) {
      Board b = boards.get(boardId);
      if (b.messages.size() > messageId) {
          post = b.messages.get(messageId).text;
      }
      
      System.out.println(messageId);
      /*for(int i = 0; i < b.messages.size(); i++) {
        System.out.println(messages.get(i).id);
      }*/
    }
        
    model.addAttribute("messageText", post);
    model.addAttribute("board", boardId);
    return "m";
  }
  
    @GetMapping("/m_form/{boardId}")
    public String addMessageForm(@PathVariable int boardId, ModelMap model) {
      model.addAttribute("board", boardId);
        return "m_form";
    }
  
    @PostMapping("/b/{boardId}/add")
  public String addMessage(@PathVariable int boardId, @RequestParam("message") String message, ModelMap model) {
    if(boards.size() <= boardId) {
      throw new ResourceNotFoundException();
    }
    
    Board b = boards.get(boardId);
    
    Message m = new Message(b.messages.size(), message);  
    b.addMessage(m, b.messages.size());
    model.addAttribute("allMessages", b.messages);
    model.addAttribute("board", boardId);
    return "m_list";
  }
  
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public class ResourceNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7963054889196792099L;}
}
