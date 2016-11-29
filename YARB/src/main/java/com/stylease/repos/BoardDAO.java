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

import com.stylease.entities.Board;
import com.stylease.entities.Key;


@Repository
public class BoardDAO extends AbstractIdDAO<Board> {

  @Autowired
  private KeyDAO keyDao;
  
  private SimpleJdbcInsert boardAdder;
  
  private static final String UPDATE_BOARD_SQL =
      "UPDATE board SET"
      + " name = ?,"
      + " enabled = ?"
      + " WHERE id = ?";
  
  private static final String DEL_BOARD_SQL =
      "DELETE FROM board WHERE id = ?";
  
  protected BoardDAO() {
    super("board", "id");
  }
  
  @Autowired
  public void setDataSource(DataSource dataSource) {
    super.setDataSource(dataSource);
    
    boardAdder = new SimpleJdbcInsert(dataSource)
      .withTableName("board")
      .usingColumns("name")
      .usingGeneratedKeyColumns("id");
  }
  
  public void addBoard(Board b) {
    HashMap<String, Object> args = new HashMap<>();
    args.put("name", b.getName());
    
    Number id = boardAdder.executeAndReturnKey(args);
    b.setId(id.longValue());
    
    List<Key> keys = b.getKeys();
    Iterator<Key> keyItr = keys.iterator();
    while(keyItr.hasNext()) {
      keyDao.addKeyToBoard(b, keyItr.next());
    }
  }
  
  public Board getForId(long id) {
    Board b = super.getForId(id, new BoardRowMapper());
    if(b == null) {
      return null;
    }
    
    b.setKeys(keyDao.getForBoard(b));
    return b;
  }
  
  public int updateBoard(Board b) {
    return this.jdbcTemplate.update(UPDATE_BOARD_SQL, b.getName(), b.getEnabled(), b.getId());
  }
  
  public int deleteBoard(Board b) {
    return this.jdbcTemplate.update(DEL_BOARD_SQL, b.getId());
  }
  
  public class BoardRowMapper implements RowMapper<Board> {
    
    @Override
    public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
      // TODO Auto-generated method stub
      Board b = new Board();
      
      b.setId(rs.getLong("id"));
      b.setName(rs.getString("name"));
      b.setCreated(rs.getDate("created"));
      b.setEnabled(rs.getBoolean("enabled"));
      
      return b;
    }
    
  }
}
