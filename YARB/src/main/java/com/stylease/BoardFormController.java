package com.stylease;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;
import com.stylease.entities.Board;
import com.stylease.entities.Key;
import com.stylease.entities.User;
import com.stylease.repos.BoardDAO;
import com.stylease.repos.KeyDAO;
import com.stylease.repos.UserDAO;

@Controller
public class BoardFormController {

  private static final String USEROP_ADD = "useradd";
  private static final String USEROP_MOD = "usermod";
  private static final String USEROP_REM = "userrem";
  
  @Autowired
  private KeyDAO keyDao;
  
  @Autowired
  private UserDAO userDao;
  
  @Autowired
  private BoardDAO boardDao;
  
  @GetMapping("/b_add")
  public String addBoardForm(HttpServletRequest req, ModelMap model) {
    //model.addAttribute("board", boardId);
    Board b = new Board();
    b.setKeys(new LinkedList<>());
    
    HttpSession sesh = req.getSession();
    sesh.setAttribute("newboard", b);
    
    sesh.setAttribute("usertbl", new HashMap<Long, User>());
    sesh.setAttribute("userkeys", new HashMap<Long, Key>());
    
    setCreateModel(model);
    model.addAttribute("usertbl", new HashMap<Long, User>());
    model.addAttribute("board", b);
    return "b_form";
  }
  
  @PostMapping(path = "/b_add", params = "saveboard")
  public String addBoardSubmit(HttpServletRequest req, HttpServletResponse resp, ModelMap model) {
    
    String boardName = req.getParameter("boardName");
    if(boardName == null || (boardName = boardName.trim()).length() == 0) {
      model.addAttribute("errors", new String[]{"You must give this board a name."});
      setCreateModel(model);
      return "b_form";
    }
    
    HttpSession sesh = req.getSession(false);
    Board board = (Board)sesh.getAttribute("newboard");
    board.setName(boardName);
    
    User currentUser = getUserFromSession(req);
    Key ownerKey = new Key();
    ownerKey.setPermission(Key.CAN_READ, true);
    ownerKey.setPermission(Key.CAN_WRITE, true);
    ownerKey.setPermission(Key.INVITE_USERS, true);
    ownerKey.setPermission(Key.ADMINISTER, true);
    ownerKey.setName(currentUser.getName() + " on " + board.getName());
    keyDao.addKey(ownerKey);
    keyDao.addKeyToUser(currentUser, ownerKey);
    
    board.addKey(ownerKey);
    currentUser.addKey(ownerKey);
    
    if(req.getParameter("ispublic") != null) {
      board.addKey(keyDao.getPublicKey());
    }
    
    HashMap<Long, User> userTbl = (HashMap<Long, User>)sesh.getAttribute("usertbl");
    HashMap<Long, Key> userKeys = (HashMap<Long, Key>)sesh.getAttribute("userkeys");
    
    for(Map.Entry<Long, Key> entry : userKeys.entrySet()) {
      Long uid = entry.getKey();
      Key k = entry.getValue();
      User u = userTbl.get(uid);
      
      k.setName(u.getName() + " on " + boardName);
      
      keyDao.addKey(k);
      board.addKey(k);
      u.addKey(k);
      keyDao.addKeyToUser(u, k);
    }
    
    boardDao.addBoard(board);
    
    sesh.removeAttribute("newboard");
    sesh.removeAttribute("usertbl");
    sesh.removeAttribute("userkeys");
    
    try {
      resp.sendRedirect("/m_list/" + board.getId());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      model.addAttribute("errors", new String[]{"Something went wrong, but the board has been created."});
      setCreateModel(model);
    }
    System.out.println("saveboard");
    
    return "b_form";
    
  }
  
  @PostMapping(path = "/b_add", params = "userop")
  public String userBoardMod(HttpServletRequest req, 
      @RequestParam("userop") String op,
      ModelMap model) {
    
    User u = null;
    HttpSession sesh = req.getSession(false);
    
    HashMap<Long, User> userTbl = (HashMap<Long, User>)sesh.getAttribute("usertbl");
    
    HashMap<Long, Key> userKeys = (HashMap<Long, Key>)sesh.getAttribute("userkeys");
    
    switch(op) { /*
    case USEROP_ADD:
      String username = req.getParameter("user");
      u = userDao.getUserForName(username);
      if(u == null) {
       model.addAttribute("errors", new String[]{"The user " + username + " does not exist."});
       break;
      }
      
      userTbl.put(u.getId(), u);
      break;
    case USEROP_MOD:
      try {
        long uid = Long.parseLong(req.getParameter("users"));
        u = userTbl.get(uid);
      }
      catch(NumberFormatException ex) {}
      break;
      */
    case USEROP_ADD:
    case USEROP_MOD:
      u = getUserFromForm(op, req, model);
      break;
    case USEROP_REM:
      try {
        long uid = Long.parseLong(req.getParameter("users"));
        userTbl.remove(uid);
        userKeys.remove(uid);
      }
      catch(NumberFormatException ex) {}
      break;
    }
    
    if(u != null) {
    
      Key k = userKeys.get(u.getId());
      if(k == null) {
        k = new Key();
        userKeys.put(u.getId(), k);
      }
      
      boolean canRead = req.getParameter("can_read") != null;
      boolean canWrite = req.getParameter("can_write") != null;
      boolean canInvite = req.getParameter("invite_users") != null;
      boolean administer = req.getParameter("administer") != null;
      
      k.setPermission(Key.CAN_READ, canRead);
      k.setPermission(Key.CAN_WRITE, canWrite);
      k.setPermission(Key.INVITE_USERS, canInvite);
      k.setPermission(Key.ADMINISTER, administer);
    }
    
    setCreateModel(model);
    //System.out.println(board.getName());
    /*System.out.println(canRead);
    System.out.println(canWrite);
    System.out.println(canInvite);
    System.out.println(administer);*/
    
    System.out.println("Op: " + op);
    
    return "b_form";
  }
  
  private User getUserFromSession(HttpServletRequest r) {
    Object o = r.getSession().getAttribute("user");
    if(o == null) {
      Account account = AccountResolver.INSTANCE.getAccount(r);
      if (account != null) {
        
        User u = userDao.getUserForStormpathAccount(account);
        if(u == null) {
          u = new User();
          u.setAccount(account);
          
          ArrayList<Key> keys = new ArrayList<>(1);
          keys.add(keyDao.getPublicKey());
          u.setKeys(keys);
          
          userDao.addUser(u);
        }
        
        r.getSession().setAttribute("user", u);
        
        return u;
      }
    }
    
    return (User)o;
  }
  
  private void setFormModel(ModelMap model, Key perms) {
    model.addAttribute("perms", perms);
    model.addAttribute("userop_add", USEROP_ADD);
    model.addAttribute("userop_mod", USEROP_MOD);
    model.addAttribute("userop_rem", USEROP_REM);
  }
  
  private void setCreateModel(ModelMap model) {
    model.addAttribute("board_action", "Create");
    model.addAttribute("submit_action", "Create");
    setFormModel(model, this.getAdderPerms());
  }
  
  private void setEditModel(ModelMap model, Key perms) {
    model.addAttribute("board_action", "Edit");
    model.addAttribute("submit_action", "Submit");
    setFormModel(model, perms);
  }
  
  @GetMapping("/m_list/{boardId}/settings")
  public String boardSettings(HttpServletRequest req, @PathVariable int boardId, ModelMap model) {
    
    Board b = getBoardOrFail(boardId);
    Key perms = this.getCurrentUserKey(req, b);
    
    if(!(perms.canInvite() || perms.isAdmin())) {
      throw new ResourceForbiddenException();
    }
    
    HashMap<Long, User> userTbl = new HashMap<>();
    HashMap<Long, Key> userKeys = new HashMap<>();
    
    Iterator<Key> keyItr = b.getKeys().iterator();
    while(keyItr.hasNext()) {
      Key k = keyItr.next();
      System.out.println("Got key " + k.getName());
      if(k.getId() == keyDao.getPublicKey().getId()) {
        model.addAttribute("ispublic", true);
        continue;
      }
      Iterator<User> userItr = userDao.getUsersForKey(k).iterator();
      while(userItr.hasNext()) {
        
        User u = userItr.next();
        
        System.out.println("Got user " + u.getName());
        
        userTbl.put(u.getId(), u);
        userKeys.put(u.getId(), k);
      }
    }
    
    HttpSession sesh = req.getSession();
    sesh.setAttribute("usertbl", userTbl);
    sesh.setAttribute("userkeys", userKeys);
    
    model.addAttribute("boardName", b.getName());
    
    setEditModel(model, perms);
    
    return "b_form";
  }
  
  @PostMapping(path = "/m_list/{boardId}/settings", params = "userop")
  public String boardSettingsUserSubmit(HttpServletRequest req,
      @PathVariable int boardId,
      @RequestParam("userop") String userop,
      ModelMap model) {
    
    Board b = getBoardOrFail(boardId);
    Key perms = this.getCurrentUserKey(req, b);
    
    if(!(perms.canInvite() || perms.isAdmin())) {
      throw new ResourceForbiddenException();
    }
    
    HttpSession sesh = req.getSession();
    HashMap<Long, User> userTbl = (HashMap<Long, User>)sesh.getAttribute("usertbl");
    HashMap<Long, Key> userKeys = (HashMap<Long, Key>)sesh.getAttribute("userkeys");
    
    if(b != null) {
      model.addAttribute("boardName", b.getName());
      
      User u = null;
      switch(userop) {
      case USEROP_MOD:
        if(!perms.isAdmin()) {
          model.addAttribute("errors", new String[]{"You are not allowed to modify user permissions on this board."});
          
          setEditModel(model, perms);
          return "b_form";
        }
      case USEROP_ADD:
        u = getUserFromForm(userop, req, model);
        break;
      case USEROP_REM:
        if(!perms.isAdmin()) {
          model.addAttribute("errors", new String[]{"You are not allowed to remove users from this board."});
          
          setEditModel(model, perms);
          return "b_form";
        }
        long uid = Long.parseLong(req.getParameter("users"));
        userTbl.remove(uid);
        Key k = userKeys.remove(uid);
        if(k != null) {
          keyDao.deleteKey(k);
        }
        break;
      }
      
      if(u != null) {
        
        Key k = userKeys.get(u.getId());
        if(k == null) {
          k = new Key();
          k.setName(u.getName() + " on " + b.getName());
          userKeys.put(u.getId(), k);
          keyDao.addKey(k);
          keyDao.addKeyToUser(u, k);
          keyDao.addKeyToBoard(b, k);
        }
        
        boolean canRead = req.getParameter("can_read") != null;
        boolean canWrite = req.getParameter("can_write") != null;
        boolean canInvite = req.getParameter("invite_users") != null;
        boolean administer = req.getParameter("administer") != null;
        
        k.setPermission(Key.CAN_READ, canRead);
        k.setPermission(Key.CAN_WRITE, canWrite);
        k.setPermission(Key.INVITE_USERS, canInvite);
        k.setPermission(Key.ADMINISTER, administer);
        
        keyDao.updateKey(k);
      }
      
    } // board exists
    else {
      throw new ResourceNotFoundException();
    }
    
    perms = this.getCurrentUserKey(req, b);
    if(!(perms.isAdmin() || perms.canInvite())) {
      throw new ResourceForbiddenException();
    }
    setEditModel(model, perms);
    return "b_form";
  }
  
  @PostMapping(path = "/m_list/{boardId}/settings", params = "saveboard")
  public String boardSettingsSubmit(HttpServletRequest req,
      HttpServletResponse resp,
      @PathVariable int boardId,
      ModelMap model) {
    
    Board b = getBoardOrFail(boardId);
    Key perms = this.getCurrentUserKey(req, b);
    
    if(!perms.isAdmin()) {
      throw new ResourceForbiddenException();
    }
    
    String boardName = req.getParameter("boardName");
    if(boardName == null || (boardName = boardName.trim()).length() == 0) {
      model.addAttribute("errors", new String[]{"You must give this board a name."});
      setEditModel(model, perms);
      return "b_form";
    }
    
    b.setName(boardName);
    boardDao.updateBoard(b);
    
    if(req.getParameter("ispublic") != null) {
      keyDao.addKeyToBoard(b, keyDao.getPublicKey());
    }
    else {
      keyDao.removeKeyFromBoard(b, keyDao.getPublicKey());
    }
    
    HttpSession sesh = req.getSession();
    sesh.removeAttribute("usertbl");
    sesh.removeAttribute("userkeys");
    
    try {
      resp.sendRedirect("/m_list/" + boardId);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      model.addAttribute("errors", new String[]{"Something went wrong, but your changes have been saved."});
      setCreateModel(model);
    }
    setEditModel(model, perms);
    return "b_form";
  }
  
  private User getUserFromForm(String userop, HttpServletRequest req, ModelMap model) {
    
    User u = null;
    HashMap<Long, User> userTbl = (HashMap<Long, User>)req.getSession().getAttribute("usertbl");
    
    switch(userop) {
    case USEROP_ADD:
      String username = req.getParameter("user");
      u = userDao.getUserForName(username);
      if(u == null) {
        model.addAttribute("errors", new String[]{"The user " + username + " does not exist."});
      }
      
      if(userTbl.containsKey(u.getId())) {
        model.addAttribute("errors", new String[]{"The user " + username + " has already been added."});
        u = null;
      }
      
      if(u != null) {
        userTbl.put(u.getId(), u);
      }
      break;
    case USEROP_MOD:
      try {
        long uid = Long.parseLong(req.getParameter("users"));
        u = userTbl.get(uid);
      }
      catch(NumberFormatException ex) {}
      break;
    }
    
    return u;
  }
  
  private Key getCurrentUserKey(HttpServletRequest req, Board b) {
    User u = getUserFromSession(req);
    return keyDao.getBoardPermissions(u, b);
  }
  
  private Board getBoardOrFail(int boardId) {
    Board b = boardDao.getForId(boardId);
    if(b == null) {
      throw new ResourceNotFoundException();
    }
    
    return b;
  }
  
  private Key getAdderPerms() {
    Key k = new Key();
    k.setPermission(Key.CAN_READ, true);
    k.setPermission(Key.CAN_WRITE, true);
    k.setPermission(Key.INVITE_USERS, true);
    k.setPermission(Key.ADMINISTER, true);
    
    return k;
  }
}