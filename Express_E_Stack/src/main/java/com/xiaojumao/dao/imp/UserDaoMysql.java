package com.xiaojumao.dao.imp;

import com.xiaojumao.bean.User;
import com.xiaojumao.dao.BaseUserDao;
import com.xiaojumao.exception.DuplicateEmailException;
import com.xiaojumao.exception.DuplicateUserNameException;
import com.xiaojumao.exception.DuplicateUserPhoneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoMysql extends JdbcDaoSupport implements BaseUserDao {

    @Autowired
    private void setMyJdbcTemplate(JdbcTemplate jdbcTemplate){
        super.setJdbcTemplate(jdbcTemplate);
    }

    // 通过邮箱查询用户信息
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM USER WHERE EMAIL=?";
    // 查询总用户数和今日新增数量
    private static final String SQL_COUNT_USER = "SELECT COUNT(ID) data_size, COUNT(TO_DAYS(REGTIME)=TO_DAYS(NOW()) OR NULL) data_day FROM USER";
    // 查询用户信息列表(分页)
    private static final String SQL_FIND_LIMIT = "SELECT * FROM USER LIMIT ?,?";
    // 查询用户信息列表(查询全部)
    private static final String SQL_FIND_ALL = "SELECT * FROM USER";
    // 根据手机号查询用户
    private static final String SQL_FIND_BY_USERPHONE = "SELECT * FROM USER WHERE USERPHONE=?";
    // 添加用户
    private static final String SQL_ADD_USER = "INSERT INTO USER(USERNAME,USERPHONE,PASSWORD,IDCARD,REGTIME,LASTTIME,LASTIP,EMAIL) VALUES(?,?,?,?,?,?,?,?)";
    // 根据用户id修改用户信息
    private static final String SQL_UPDATE_USER = "UPDATE USER SET USERNAME=?,USERPHONE=?,IDCARD=?,PASSWORD=?,EMAIL=? WHERE ID=?";
    // 根据邮箱修改用户信息
    private static final String SQL_UPDATE_USER_BY_EMAIL = "UPDATE USER SET USERNAME=?,USERPHONE=?,IDCARD=?,PASSWORD=?,EMAIL=? WHERE EMAIL=?";
    private static final String SQL_DELETE_USER = "DELETE FROM USER WHERE ID=?";
    // 检查邮箱和密码是否匹配
    private static final String SQL_CHECK_BY_EMAIL_AND_PASSWORD = "SELECT * FROM USER WHERE EMAIL=? AND PASSWORD=?";

    /**
     * 自定义结果处理方法
     */
    private User handleResult(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setUserPhone(resultSet.getString("userPhone"));
        user.setPassword(resultSet.getString("password"));
        user.setIdCard(resultSet.getString("idcard"));
        user.setRegTime(resultSet.getTimestamp("regtime"));
        user.setLastTime(resultSet.getTimestamp("lasttime"));
        user.setLastIp(resultSet.getString("lastip"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }

    /**
     * 通过邮箱查询用户信息
     *
     * @param email by邮箱
     * @return 用户信息, 查询不到返回null
     */
    @Override
    public User findByEmail(String email) {
        User user = null;
        try {
            user = this.getJdbcTemplate().queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email},
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleResult(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 查询用户信息列表(分页)
     *
     * @param limit    0分页(默认), 1查询全部
     * @param offset   从该位置开始查询
     * @param pageSize 最多查询的条数
     * @return 用户信息列表, 否则返回null
     */
    @Override
    public List<User> findAll(boolean limit, int offset, int pageSize) {
        if(limit){
            return this.getJdbcTemplate().query(SQL_FIND_LIMIT, new Object[]{offset,pageSize},
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleResult(resultSet);
                        }
                    });
        }else{
            return this.getJdbcTemplate().query(SQL_FIND_ALL,
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleResult(resultSet);
                        }
                    });
        }
    }

    /**
     * 查询总用户数和今日新增数量
     *
     * @return
     */
    @Override
    public Map<String, Integer> console() {
        Map<String, Object> stringObjectMap = this.getJdbcTemplate().queryForMap(SQL_COUNT_USER);
        Map<String, Integer> map = new HashMap<>();
        map.put("data_size", Integer.parseInt(stringObjectMap.get("data_size").toString()));
        map.put("data_day", Integer.parseInt(stringObjectMap.get("data_day").toString()));
        return map;
    }

    /**
     * 根据手机号查询用户
     *
     * @param userPhone 用户
     * @return 查询到的用户信息, 否则返回null
     */
    @Override
    public User findByUserPhone(String userPhone) {
        User user = null;
        try {
            user = this.getJdbcTemplate().queryForObject(SQL_FIND_BY_USERPHONE, new Object[]{userPhone},
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleResult(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 添加用户
     *
     * @param u 添加的用户信息
     * @return true:添加成功,false:添加失败
     */
    @Override
    public boolean add(User u) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateEmailException {
        int update = this.getJdbcTemplate().update(SQL_ADD_USER, u.getUserName(), u.getUserPhone(), u.getPassword(), u.getIdCard(), u.getRegTime(),
                u.getLastTime(), u.getLastIp(), u.getEmail());
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 修改用户信息
     *
     * @param id   待修改的用户id
     * @param newu 新用户的信息
     * @return true:修改成功, false:修改失败
     */
    @Override
    public boolean update(int id, User newu) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateEmailException {
        int update = this.getJdbcTemplate().update(SQL_UPDATE_USER, newu.getUserName(), newu.getUserPhone(), newu.getIdCard(), newu.getPassword(),
                newu.getEmail(), id);
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 修改用户信息
     *
     * @param email 待修改的用户邮箱
     * @param newu  新用户的信息
     * @return true:修改成功, false:修改失败
     */
    @Override
    public boolean update(String email, User newu) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateEmailException {
        int update = this.getJdbcTemplate().update(SQL_UPDATE_USER_BY_EMAIL, newu.getUserName(), newu.getUserPhone(), newu.getIdCard(), newu.getPassword(),
                newu.getEmail(), email);
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 根据用户id删除用户信息
     *
     * @param id 待删除的用户id
     * @return true:删除成功, false:删除失败
     */
    @Override
    public boolean delete(int id) {
        int update = this.getJdbcTemplate().update(SQL_DELETE_USER, id);
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 检查邮箱和密码是否匹配
     *
     * @param email    : 邮箱
     * @param password : 密码
     * @return 匹配返回用户信息, 失败返回null
     */
    @Override
    public User checkEmailAndPassword(String email, String password) {
        User user = null;
        try {
            user = this.getJdbcTemplate().queryForObject(SQL_CHECK_BY_EMAIL_AND_PASSWORD, new Object[]{email, password},
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleResult(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return user;
    }
}
