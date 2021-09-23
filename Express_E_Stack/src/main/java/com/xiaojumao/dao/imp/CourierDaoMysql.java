package com.xiaojumao.dao.imp;

import com.xiaojumao.bean.Courier;
import com.xiaojumao.dao.BaseCourierDao;
import com.xiaojumao.exception.DuplicateCNameException;
import com.xiaojumao.exception.DuplicateCPhoneException;
import com.xiaojumao.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class CourierDaoMysql extends JdbcDaoSupport implements BaseCourierDao {

    @Autowired
    private void setMyJdbcTemplate(JdbcTemplate jdbcTemplate){
        super.setJdbcTemplate(jdbcTemplate);
    }

    // 查询总用户数和今日新增数量
    private static final String SQL_COUNT_USER = "SELECT COUNT(ID) data_size, COUNT(TO_DAYS(REGTIME)=TO_DAYS(NOW()) OR NULL) data_day FROM COURIER";
    // 查询快递员信息列表(分页)
    private static final String SQL_FIND_LIMIT = "SELECT * FROM COURIER LIMIT ?,?";
    // 查询快递员信息列表(查询全部)
    private static final String SQL_FIND_ALL = "SELECT * FROM COURIER";
    // 通过邮箱查询快递员信息
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM COURIER WHERE EMAIL=?";
    // 根据手机号查询快递员
    private static final String SQL_FIND_BY_CPHONE = "SELECT * FROM COURIER WHERE CPHONE=?";
    // 添加快递员
    private static final String SQL_ADD_COURIER = "INSERT INTO COURIER(CNAME,CPHONE,PASSWORD,SENDNUM,IDCARD,REGTIME,LASTTIME,LASTIP,EMAIL) VALUES(?,?,?,?,?,?,?,?,?)";
    // 根据id修改快递员
    private static final String SQL_UPDATE_BY_ID = "UPDATE COURIER SET CNAME=?,CPHONE=?,IDCARD=?,PASSWORD=?,EMAIL=? WHERE ID=?";
    // 根据邮箱修改快递员
    private static final String SQL_UPDATE_BY_EMAIL = "UPDATE COURIER SET CNAME=?,CPHONE=?,IDCARD=?,PASSWORD=?,EMAIL=? WHERE EMAIL=?";
    // 根据id删除快递员
    private static final String SQL_DELETE_BY_ID = "DELETE FROM COURIER WHERE ID=?";
    // 检查邮箱和密码是否匹配
    private static final String SQL_CHECK_EMAIL_PASSWORD = "SELECT * FROM COURIER WHERE EMAIL=? AND PASSWORD=?";


    /**
     * 自己封装处理结果的方法
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Courier handleCourierResult(ResultSet resultSet) throws SQLException {
        Courier courier = new Courier();
        courier.setLastTime(resultSet.getTimestamp("lasttime"));
        courier.setRegTime(resultSet.getTimestamp("regtime"));
        courier.setcName(resultSet.getString("cname"));
        courier.setcPhone(resultSet.getString("cphone"));
        courier.setEmail(resultSet.getString("email"));
        courier.setSendNum(resultSet.getString("sendnum"));
        courier.setLastIp(resultSet.getString("lastip"));
        courier.setId(resultSet.getInt("id"));
        courier.setPassword(resultSet.getString("password"));
        courier.setIdCard(resultSet.getString("idcard"));
        return courier;
    }

    /**
     * 通过邮箱查询快递员信息
     *
     * @param email by邮箱
     * @return 快递员信息, 查询不到返回null
     */
    @Override
    public Courier findByEmail(String email) {
        Courier courier = null;
        try {
            courier = this.getJdbcTemplate().queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email},
                    new RowMapper<Courier>() {
                        @Override
                        public Courier mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleCourierResult(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return courier;
    }

    /**
     * 查询总快递员数和今日新增数量
     *
     * @return
     */
    @Override
    public Map<String, Integer> console() {
        Map<String, Object> stringObjectMap = this.getJdbcTemplate().queryForMap(SQL_COUNT_USER);
        Set<String> strings = stringObjectMap.keySet();
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String string : strings) {
            Integer i = Integer.parseInt(stringObjectMap.get(string).toString());
            map.put(string, i);
        }
        return map;
    }

    /**
     * 查询快递员信息列表(分页)
     *
     * @param limit    0分页(默认), 1查询全部
     * @param offset   从该位置开始查询
     * @param pageSize 最多查询的条数
     * @return 快递员信息列表, 否则返回null
     */
    @Override
    public List<Courier> findAll(boolean limit, int offset, int pageSize) {
        if(limit) {
            return this.getJdbcTemplate().query(SQL_FIND_LIMIT, new Object[]{offset, pageSize},
                new RowMapper<Courier>() {
                    @Override
                    public Courier mapRow(ResultSet resultSet, int i) throws SQLException {
                        return handleCourierResult(resultSet);
                    }
                });
        }else{
            return this.getJdbcTemplate().query(SQL_FIND_ALL,
                    new RowMapper<Courier>() {
                        @Override
                        public Courier mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleCourierResult(resultSet);
                        }
                    });
        }
    }

    /**
     * 根据手机号查询快递员
     *
     * @param cPhone 快递员
     * @return 查询到的快递员信息, 否则返回null
     */
    @Override
    public Courier findByCPhone(String cPhone) {
        Courier courier = null;
        try {
            courier = this.getJdbcTemplate().queryForObject(SQL_FIND_BY_CPHONE, new Object[]{cPhone},
                    new RowMapper<Courier>() {
                        @Override
                        public Courier mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleCourierResult(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return courier;
    }

    /**
     * 添加快递员
     *
     * @param u 添加的快递员信息
     * @return true:添加成功,false:添加失败
     */
    @Override
    public boolean add(Courier u) throws DuplicateCNameException, DuplicateCPhoneException {
        int update = this.getJdbcTemplate().update(SQL_ADD_COURIER,
                new Object[]{u.getcName(), u.getcPhone(), u.getPassword(), u.getSendNum(),
                        u.getIdCard(), u.getRegTime(), u.getLastTime(), u.getLastIp(), u.getEmail()});
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 修改快递员信息
     *
     * @param id   待修改的快递员id
     * @param newc 新快递员的信息
     * @return true:修改成功, false:修改失败
     */
    @Override
    public boolean update(int id, Courier newc) throws DuplicateCNameException, DuplicateCPhoneException, DuplicateEmailException {
        int update = this.getJdbcTemplate().update(SQL_UPDATE_BY_ID, new Object[]{newc.getcName(), newc.getcPhone(), newc.getIdCard(),
                newc.getPassword(), newc.getEmail(), id});
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 修改快递员信息
     *
     * @param email 待修改的快递员邮箱
     * @param newc  新快递员的信息
     * @return true:修改成功, false:修改失败
     */
    @Override
    public boolean update(String email, Courier newc) throws DuplicateCNameException, DuplicateCPhoneException, DuplicateEmailException {
        int update = this.getJdbcTemplate().update(SQL_UPDATE_BY_EMAIL, new Object[]{newc.getcName(), newc.getcPhone(), newc.getIdCard(),
                newc.getPassword(), newc.getEmail(), email});
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 根据快递员id删除快递员信息
     *
     * @param id 待删除的快递员id
     * @return true:删除成功, false:删除失败
     */
    @Override
    public boolean delete(int id) {
        int update = this.getJdbcTemplate().update(SQL_DELETE_BY_ID, id);
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 检查邮箱和密码是否匹配
     *
     * @param email    : 邮箱
     * @param password : 密码
     * @return 匹配返回快递员信息, 失败返回null
     */
    @Override
    public Courier checkEmailAndPassword(String email, String password) {
        Courier courier = null;
        try {
            courier =  this.getJdbcTemplate().queryForObject(SQL_CHECK_EMAIL_PASSWORD, new Object[]{email, password},
                    new RowMapper<Courier>() {
                        @Override
                        public Courier mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleCourierResult(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return courier;
    }
}
