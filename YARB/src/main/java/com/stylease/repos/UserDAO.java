package com.stylease.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.stormpath.sdk.account.Account;
import com.stylease.entities.Key;
import com.stylease.entities.User;
import com.stylease.repos.KeyDAO.KeyRowMapper;

@Repository
public class UserDAO extends AbstractIdDAO<User> {

  private static final String USER_FOR_NAME_SQL =
      "SELECT * FROM app_user WHERE stormpath_username = ?";
  
  private static final String USERS_FOR_KEY_SQL =
      "SELECT u.* FROM app_user u"
      + " INNER JOIN user_keys uk ON u.id = uk.userid"
      + " WHERE uk.keyid = ?";
  
  private static final String ADD_USER_SQL =
      "INSERT INTO app_user (stormpath_username) VALUES (?)";
  
  private static final String USER_ID_COL = "id";
  private static final String USER_NAME_COL = "stormpath_username";
  
  @Autowired
  private KeyDAO keyDao;
  
  private SimpleJdbcInsert userAdder;
  
  public UserDAO() {
    super("app_user", USER_ID_COL);
  }
  
  @Autowired
  public void setDataSource(DataSource dataSource) {
    super.setDataSource(dataSource);
    
    userAdder = new SimpleJdbcInsert(dataSource)
      .withTableName("app_user")
      .usingColumns(USER_NAME_COL)
      .usingGeneratedKeyColumns(USER_ID_COL);
  }
  
  public User getUserForName(String name) {
    List<User> lUser = this.jdbcTemplate.query(USER_FOR_NAME_SQL, new Object[]{name}, new UserRowMapper());
    
    if(lUser.size() == 0) {
      return null;
    }
    
    User u = lUser.get(0);
    u.setKeys(keyDao.getForUser(u));
    
    return u;
  }
  
  public User getUserForStormpathAccount(Account acct) {
    User u = getUserForName(acct.getUsername());
    if(u == null) {
      return null;
    }
    u.setAccount(acct);
    return u;
  }
  
  public void addUser(User u) {
    HashMap<String, Object> args = new HashMap<>();
    args.put(USER_NAME_COL, u.getName());
    
    Number id = userAdder.executeAndReturnKey(args);
    u.setId(id.longValue());
    
    List<Key> keys = u.getKeys();
    Iterator<Key> keyItr = keys.iterator();
    while(keyItr.hasNext()) {
      keyDao.addKeyToUser(u, keyItr.next());
    }
  }
  
  public List<User> getUsersForKey(Key k) {
    return this.jdbcTemplate.query(USERS_FOR_KEY_SQL, new Object[]{k.getId()}, new UserRowMapper());
  }
  
  public class UserRowMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      // TODO Auto-generated method stub
      User user = new User();
      user.setId(rs.getLong("id"));
      user.setName(rs.getString("stormpath_username"));
      
      return user;
    }
    
  }
  
  public void addUser(String name) {
    
  }
}
