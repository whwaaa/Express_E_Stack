package com.xiaojumao.dao.imp;

import com.xiaojumao.bean.Express;
import com.xiaojumao.bean.LazyInfo;
import com.xiaojumao.dao.BaseExpressDao;
import com.xiaojumao.exception.DuplicateCodeException;
import com.xiaojumao.exception.DuplicateNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class ExpressDaoMysql extends JdbcDaoSupport implements BaseExpressDao {

    @Autowired
    private void setMyJdbcTemplate(JdbcTemplate jdbcTemplate){
        super.setJdbcTemplate(jdbcTemplate);
    }

    // 用于获取控制台所需的快递数据,(包含总快递数和未取快递数,size:总数,day:当日新增)
    private static final String SQL_CONSOLE = "SELECT COUNT(ID) data1_size, COUNT(TO_DAYS(INTIME)=TO_DAYS(NOW()) OR NULL) data1_day, COUNT(STATUS=0 OR NULL) data2_size, COUNT(TO_DAYS(INTIME)=TO_DAYS(NOW()) AND STATUS=0 OR NULL) data2_day FROM EXPRESS";
    // 快件列表查询
    private static final String SQL_FIND_ALL = "SELECT * FROM EXPRESS";
    // 快件列表查询(分页查询)
    private static final String SQL_FIND_LIMIT = "SELECT * FROM EXPRESS LIMIT ?,?";
    // 根据快递单号查询快递信息
    private static final String SQL_FIND_BY_NUMBER = "SELECT * FROM EXPRESS WHERE NUMBER=?";
    // 根据邮箱查询快递信息
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM EXPRESS WHERE EMAIL=?";
    // 根据取件码查询快递信息
    private static final String SQL_FIND_BY_CODE = "SELECT * FROM EXPRESS WHERE CODE=?";
    // 根据id查询快递信息
    private static final String SQL_FIND_BY_ID = "SELECT * FROM EXPRESS WHERE ID=?";
    // 根据用户的手机号，查询快递信息,status:0表示查询待取件的快递(默认),1表示查询已取件的快递,2表示查询用户所有快递
    private static final String SQL_FIND_BY_USERPHONE_STATUS0 = "SELECT * FROM EXPRESS WHERE USERPHONE=? AND STATUS=0";
    private static final String SQL_FIND_BY_USERPHONE_STATUS1 = "SELECT * FROM EXPRESS WHERE USERPHONE=? AND STATUS=1";
    private static final String SQL_FIND_BY_USERPHONE_STATUS2 = "SELECT * FROM EXPRESS WHERE USERPHONE=?";
    // 根据录入人的手机号，查询快递信息
    private static final String SQL_FIND_BY_SYSPHONE = "SELECT * FROM EXPRESS WHERE SYSPHONE=?";
    // 快递录入
    private static final String SQL_INSERT_EXPRESS = "INSERT INTO EXPRESS(NUMBER,USERNAME,USERPHONE,COMPANY,CODE,INTIME,SYSPHONE,STATUS,EMAIL) VALUES(?,?,?,?,?,?,?,?,?)";
    // 根据id修改快递出信息
    private static final String SQL_UPDATE_EXPRESS = "UPDATE EXPRESS SET NUMBER=?,COMPANY=?,USERNAME=?,USERPHONE=?,STATUS=?,CODE=?,EMAIL=?,OUTTIME=? WHERE ID=?";
    // 根据id删除快递信息
    private static final String SQL_DELETE_BY_ID = "DELETE FROM EXPRESS WHERE ID=?";
    // 根据快递单号修改快递为已取件状态
    private static final String SQL_UPDATE_STUTS_BY_NUMBER = "UPDATE EXPRESS SET STATUS=1,CODE=NULL,OUTTIME=? WHERE CODE=?";
    // 获取快递数量排名前三的用户信息 一年内/一个月内/所有
    private static final String SQL_FIND_EXPRESS_NUM_MAX_THREE_USER_ONE_YEAR = "SELECT USERNAME,COUNT(EMAIL),EMAIL FROM EXPRESS WHERE DATEDIFF(INTIME,NOW())>-365 GROUP BY EMAIL,USERNAME ORDER BY COUNT(EMAIL) DESC LIMIT 0,6";
    private static final String SQL_FIND_EXPRESS_NUM_MAX_THREE_USER_ONE_MONTH = "SELECT USERNAME,COUNT(EMAIL),EMAIL FROM EXPRESS WHERE DATEDIFF(INTIME,NOW())>-30 GROUP BY EMAIL,USERNAME ORDER BY COUNT(EMAIL) DESC LIMIT 0,6";
    private static final String SQL_FIND_EXPRESS_NUM_MAX_THREE_USER_ALL = "SELECT USERNAME,COUNT(EMAIL),EMAIL FROM EXPRESS GROUP BY EMAIL,USERNAME ORDER BY COUNT(EMAIL) DESC LIMIT 0,6";

    /**
     * 自己封装处理结果的方法
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Express handleReault(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            Express express = new Express();
            express.setId(resultSet.getInt("id"));
            express.setNumber(resultSet.getString("number"));
            express.setUserName(resultSet.getString("username"));
            express.setUserPhone(resultSet.getString("userphone"));
            express.setCompany(resultSet.getString("company"));
            express.setCode(resultSet.getString("code"));
            express.setInTime(resultSet.getTimestamp("intime"));
            express.setOutTime(resultSet.getTimestamp("outtime"));
            express.setStatus(resultSet.getInt("status"));
            express.setSysPhone(resultSet.getString("sysphone"));
            express.setEmail(resultSet.getString("email"));
            return express;
        }
        return null;
    }

    /**
     * 获取快递数量排名前三的用户信息
     *
     * @param timeType 0:总排名 1:年排名 2:月排名
     * @return 排名前三的用户信息
     */
    @Override
    public List<LazyInfo> getLazyboard(Integer timeType) {
        List<LazyInfo> lazyInfoList = new ArrayList<>();
        List<Map<String, Object>> maps = null;
        switch(timeType){
            case 0:
                maps = this.getJdbcTemplate().queryForList(SQL_FIND_EXPRESS_NUM_MAX_THREE_USER_ALL);
                break;
            case 1:
                maps = this.getJdbcTemplate().queryForList(SQL_FIND_EXPRESS_NUM_MAX_THREE_USER_ONE_YEAR);
                break;
            case 2:
                maps = this.getJdbcTemplate().queryForList(SQL_FIND_EXPRESS_NUM_MAX_THREE_USER_ONE_MONTH);
                break;
        }
        try {
            for (Map<String, Object> map : maps) {
                Set<String> keySet = map.keySet();
                Iterator<String> iterator = keySet.iterator();
                LazyInfo lazyInfo = new LazyInfo();
                while(iterator.hasNext()){
                    String next = iterator.next();
                    if(next.equals("USERNAME")){
                        lazyInfo.setUsername(map.get(next)==null?null:map.get(next).toString());
                    }else if(next.equals("COUNT(EMAIL)")){
                        lazyInfo.setNum(Integer.parseInt(map.get(next).toString()));
                    }
                }
                lazyInfoList.add(lazyInfo);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            System.out.println("-------");
        }
        return lazyInfoList;
    }

    /**
     * 用于获取控制台所需的快递数据,(包含总快递数和未取快递数,size:总数,day:当日新增)
     *
     * @return [{size:1000,day:100},{size:500,day:100}]
     */
    @Override
    public List<Map<String, Integer>> console() {
        Map<String, Object> stringObjectMap = this.getJdbcTemplate().queryForMap(SQL_CONSOLE);
        List<Map<String, Integer>> list = new ArrayList<>();
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        map1.put("data1_size",Integer.parseInt(stringObjectMap.get("data1_size").toString()));
        map1.put("data1_day",Integer.parseInt(stringObjectMap.get("data1_day").toString()));
        map2.put("data2_size",Integer.parseInt(stringObjectMap.get("data2_size").toString()));
        map2.put("data2_day",Integer.parseInt(stringObjectMap.get("data2_day").toString()));
        list.add(map1);
        list.add(map2);
        return list;
    }

    /**
     * 快件列表查询(分页查询)
     *
     * @param limit      true表示开启分页(默认),false表示查询所有
     * @param offset     SQL语句查询起始索引
     * @param pageNumber 查询的数量
     * @return 查询到的快递信息列表
     */
    @Override
    public List<Express> findAll(boolean limit, int offset, int pageNumber) {
        if(limit){ // 开启分页
            return this.getJdbcTemplate().query(SQL_FIND_LIMIT, new Object[]{offset,pageNumber},
                    new RowMapper<Express>() {
                        @Override
                        public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleReault(resultSet);
                        }
                    });
        }else{     // 查询全部
            return this.getJdbcTemplate().query(SQL_FIND_ALL,
                    new RowMapper<Express>() {
                        @Override
                        public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleReault(resultSet);
                        }
                    });
        }
    }

    /**
     * 根据快递单号查询快递信息
     *
     * @param number 快递单号
     * @return 查询到的快递信息
     */
    @Override
    public Express findByNumber(String number) {
        Express express = null;
        try {
            express = this.getJdbcTemplate().queryForObject(SQL_FIND_BY_NUMBER, new Object[]{number},
                    new RowMapper<Express>() {
                        @Override
                        public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleReault(resultSet);
                        }
            });
        } catch (Exception e) {
        }
        return express;
    }

    /**
     * 根据id快递信息
     *
     * @param id 快递id
     * @return 查询到的快递信息
     */
    @Override
    public Express findById(Integer id) {
        Express express = null;
        try {
            express = this.getJdbcTemplate().queryForObject(SQL_FIND_BY_ID, new Object[]{id},
                    new RowMapper<Express>() {
                        @Override
                        public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleReault(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return express;
    }

    /**
     * 根据邮箱查询快递信息
     *
     * @param email 快递单号
     * @return 查询到的快递信息
     */
    @Override
    public List<Express> findByEmail(String email) {
        return this.getJdbcTemplate().query(SQL_FIND_BY_EMAIL, new Object[]{email},
                new RowMapper<Express>() {
                    @Override
                    public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                        return handleReault(resultSet);
                    }
                });
    }

    /**
     * 根据取件码查询快递信息
     *
     * @param code 取件码
     * @return 查询到的快递信息
     */
    @Override
    public Express findByCode(String code) {
        Express express = null;
        try {
            express = this.getJdbcTemplate().queryForObject(SQL_FIND_BY_CODE, new Object[]{code},
                    new RowMapper<Express>() {
                        @Override
                        public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                            return handleReault(resultSet);
                        }
                    });
        } catch (Exception e) {
        }
        return express;
    }

    /**
     * 根据用户手机号查询用户所有快递信息
     *
     * @param userPhone 用户手机号
     * @param status    0表示查询待取件的快递(默认)
     *                  1表示查询已取件的快递
     *                  2表示查询用户所有快递
     * @return 查询到的快递信息列表
     */
    @Override
    public List<Express> findByUserPhone(String userPhone, int status) {
        switch(status){
            case 0:
                return this.getJdbcTemplate().query(SQL_FIND_BY_USERPHONE_STATUS0, new Object[]{userPhone},
                        new RowMapper<Express>() {
                            @Override
                            public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                                return handleReault(resultSet);
                            }
                        });
            case 1:
                return this.getJdbcTemplate().query(SQL_FIND_BY_USERPHONE_STATUS1, new Object[]{userPhone},
                        new RowMapper<Express>() {
                            @Override
                            public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                                return handleReault(resultSet);
                            }
                        });
            case 2:
                return this.getJdbcTemplate().query(SQL_FIND_BY_USERPHONE_STATUS2, new Object[]{userPhone},
                        new RowMapper<Express>() {
                            @Override
                            public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                                return handleReault(resultSet);
                            }
                        });
        }
        return null;
    }

    /**
     * 根据录入人手机号查询快递信息
     *
     * @param sysPhone 录入人手机号
     * @return 查询到的快递信息列表
     */
    @Override
    public List<Express> findBySysPhone(String sysPhone) {
        return this.getJdbcTemplate().query(SQL_FIND_BY_SYSPHONE, new Object[]{sysPhone},
                new RowMapper<Express>() {
                    @Override
                    public Express mapRow(ResultSet resultSet, int i) throws SQLException {
                        return handleReault(resultSet);
                    }
                });
    }

    /**
     * 快递录入
     *
     * @param e 封装好的快递信息
     * @return true表示录入成功, false表示录入失败
     */
    @Override
    public boolean insert(Express e){
        int update = this.getJdbcTemplate().update(SQL_INSERT_EXPRESS, e.getNumber(), e.getUserName(), e.getUserPhone(),
                e.getCompany(), e.getCode(), e.getInTime(), e.getSysPhone(), e.getStatus(), e.getEmail());
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 根据id修改快递出信息
     *
     * @param id         要修改的快递id
     * @param newExpress 新的快递信息
     * @return true表示修改成功, false表示修改失败
     */
    @Override
    public boolean update(int id, Express newExpress) {
        int update = this.getJdbcTemplate().update(SQL_UPDATE_EXPRESS, newExpress.getNumber(), newExpress.getCompany(),
                newExpress.getUserName(), newExpress.getUserPhone(), newExpress.getStatus(), newExpress.getCode(),
                newExpress.getEmail(), newExpress.getOutTime(), id);
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 根据id删除快递信息
     *
     * @param id 要删除的快递id
     * @return true表示删除成功, false表示删除失败
     */
    @Override
    public boolean delete(int id) {
        int update = this.getJdbcTemplate().update(SQL_DELETE_BY_ID, id);
        if(update > 0)
            return true;
        return false;
    }

    /**
     * 根据快递单号修改快递为已取件状态
     *
     * @param code    要修改的快递单号
     * @param outTime 出货时间
     * @return true表示修改成功, false表示修改失败
     */
    @Override
    public boolean updateStatus(String code, Timestamp outTime) {
        int update = this.getJdbcTemplate().update(SQL_UPDATE_STUTS_BY_NUMBER, outTime, code);
        if(update > 0)
            return true;
        return false;
    }
}
