package com.stylease.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.stylease.entities.Board;
import com.stylease.entities.Key;
import com.stylease.entities.User;

@Repository
public class KeyDAO extends AbstractIdDAO<Key> {
  
  private static final String KEYS_FOR_USER_SQL =
      "SELECT k.* FROM app_key k"
      + " INNER JOIN user_keys uk ON k.id = uk.keyid"
      + " INNER JOIN app_user u ON uk.userid = u.id"
      + " WHERE u.id = ?";
  
  private static final String KEYS_FOR_BOARD_SQL =
      "SELECT k.* FROM app_key k"
      + " INNER JOIN board_keys bk ON k.id = bk.keyid"
      + " INNER JOIN board b ON bk.boardid = b.id"
      + " WHERE b.id = ?";
  
  private static final String KEY_FOR_NAME_SQL =
      "SELECT * FROM app_key WHERE name = ?";
  
  private static final String UPDATE_KEY_SQL =
      "UPDATE app_key SET"
      + " name = ?,"
      + " can_read = ?,"
      + " can_write = ?,"
      + " invite_users = ?,"
      + " administer = ?"
      + " WHERE id = ?";
  
  private static final String DELETE_KEY_SQL =
      "DELETE FROM app_key WHERE id = ?";
  
  private static final String REMOVE_KEY_FROM_BOARD_SQL =
      "DELETE FROM board_keys WHERE boardid = ? AND keyid = ?";
  
  private static final String KEY_FOR_BOARD_SQL =
      "SELECT k.* FROM app_key k"
      + " INNER JOIN board_keys bk ON k.id = bk.keyid"
      + " WHERE k.id = ?"
      + " AND bk.boardid = ?";
  
  private static final String USER_BOARD_KEYS_SQL =
      "SELECT k.* FROM app_key k"
      + " INNER JOIN board_keys bk ON k.id = bk.keyid"
      + " INNER JOIN user_keys uk ON bk.keyid = uk.keyid"
      + " WHERE uk.userid = ?"
      + " AND bk.boardid = ?";
  
  private static final String ADD_KEY_TO_USER_SQL =
      "INSERT INTO user_keys VALUES (?, ?)";
  
  private static final String BOARDID_COL = "boardid";
  private static final String USERID_COL = "userid";
  private static final String KEYID_COL = "keyid";
  
  private static Key publicKey = null;
  
  private SimpleJdbcInsert userKeyAdder;
  private SimpleJdbcInsert boardKeyAdder;
  private SimpleJdbcInsert keyAdder;
  
  public KeyDAO() {
    super("app_key", "id");
  }
  
  @Autowired
  public void setDataSource(DataSource dataSource) {
    super.setDataSource(dataSource);
    
    keyAdder = new SimpleJdbcInsert(dataSource)
      .withTableName("app_key")
      .usingColumns("name", "can_read", "can_write", "invite_users", "administer")
      .usingGeneratedKeyColumns("id");
    
    userKeyAdder = new SimpleJdbcInsert(dataSource)
      .withTableName("user_keys")
      .usingColumns(USERID_COL, KEYID_COL);
    
    boardKeyAdder = new SimpleJdbcInsert(dataSource)
      .withTableName("board_keys")
      .usingColumns(BOARDID_COL, KEYID_COL);
  }
  
  public Key getForId(long id) {
    return super.getForId(id, new KeyRowMapper());
  }
  
  public Key getKeyForName(String name) {
    return this.jdbcTemplate.queryForObject(KEY_FOR_NAME_SQL, new Object[]{name}, new KeyRowMapper());
  }
  
  public List<Key> getForUser(User u) {
    return this.jdbcTemplate.query(KEYS_FOR_USER_SQL, new Object[]{u.getId()}, new KeyRowMapper());
  }
  
  public List<Key> getForBoard(Board b) {
    return this.jdbcTemplate.query(KEYS_FOR_BOARD_SQL, new Object[]{b.getId()}, new KeyRowMapper());
  }
  
  public void addKey(Key k) {
    HashMap<String, Object> args = new HashMap<>();
    args.put("name", k.getName());
    args.put("can_read", k.canRead());
    args.put("can_write", k.canWrite());
    args.put("invite_users", k.canInvite());
    args.put("administer", k.isAdmin());
    
    Number id = keyAdder.executeAndReturnKey(args);
    k.setId(id.longValue());
  }
  
  public void addKeyToUser(User u, Key k) {
    HashMap<String, Object> args = new HashMap<>();
    args.put(USERID_COL, u.getId());
    args.put(KEYID_COL, k.getId());
    
    userKeyAdder.execute(args);
  }
  
  public void addKeyToBoard(Board b, Key k) {
    Key prevKey = this.getKeyForBoard(b, k);
    
    if(prevKey == null) {
      HashMap<String, Object> args = new HashMap<>();
      args.put(BOARDID_COL, b.getId());
      args.put(KEYID_COL, k.getId());
      
      boardKeyAdder.execute(args);
    }
  }
  
  public int updateKey(Key k) {
    
    return this.jdbcTemplate.update(UPDATE_KEY_SQL,
        k.getName(),
        k.canRead(),
        k.canWrite(),
        k.canInvite(),
        k.isAdmin(),
        k.getId());
  }
  
  public int deleteKey(Key k) {
    return this.jdbcTemplate.update(DELETE_KEY_SQL, k.getId());
  }
  
  public int removeKeyFromBoard(Board b, Key k) {
    return this.jdbcTemplate.update(REMOVE_KEY_FROM_BOARD_SQL, b.getId(), k.getId());
  }
  
  public Key getKeyForBoard(Board b, Key k) {
    Object[] args = {k.getId(), b.getId()};
    List<Key> lstKeys = this.jdbcTemplate.query(KEY_FOR_BOARD_SQL, args, new KeyRowMapper());
    if(lstKeys.size() == 0) {
      return null;
    }
    
    return lstKeys.get(0);
  }
  
  public Key getBoardPermissions(User u, Board b) {
    Key k = new Key();
    List<Key> keyList = this.jdbcTemplate.query(this.USER_BOARD_KEYS_SQL, 
        new Object[]{u.getId(), b.getId()},
        new KeyRowMapper()
    );
    
    Iterator<Key> itr = keyList.iterator();
    while(itr.hasNext()) {
      Key current = itr.next();
      boolean can_read = current.canRead() || k.canRead();
      boolean can_write = current.canWrite() || k.canWrite();
      boolean invite_users = current.canInvite() || k.canInvite();
      boolean administer = current.isAdmin() || k.isAdmin();
      
      k.setPermission(Key.CAN_READ, can_read);
      k.setPermission(Key.CAN_WRITE, can_write);
      k.setPermission(Key.INVITE_USERS, invite_users);
      k.setPermission(Key.ADMINISTER, administer);
    }
    
    return k;
  }
  
  public class KeyRowMapper implements RowMapper<Key> {
    
    @Override
    public Key mapRow(ResultSet rs, int rowNum) throws SQLException {
      // TODO Auto-generated method stub
      Key key = new Key();
      key.setId(rs.getLong("id"));
      key.setName(rs.getString("name"));
      key.setPermission(Key.CAN_READ, rs.getBoolean("can_read"));
      key.setPermission(Key.CAN_WRITE, rs.getBoolean("can_write"));
      key.setPermission(Key.INVITE_USERS, rs.getBoolean("invite_users"));
      key.setPermission(Key.ADMINISTER, rs.getBoolean("administer"));
      
      return key;
    }
    
  }
  
  public Key getPublicKey() {
    if(publicKey == null) {
      publicKey = getKeyForName("Public");
    }
    
    return publicKey;
  }
  
}
