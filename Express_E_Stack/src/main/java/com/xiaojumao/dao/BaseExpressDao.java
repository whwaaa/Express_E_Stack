package com.xiaojumao.dao;

import com.xiaojumao.bean.Express;
import com.xiaojumao.bean.LazyInfo;
import com.xiaojumao.exception.DuplicateCodeException;
import com.xiaojumao.exception.DuplicateNumberException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-01 11:43
 * @Modified By:
 */
public interface BaseExpressDao {

    /**
     * 获取快递数量排名前三的用户信息
     * @param timeType 0:总排名 1:年排名 2:月排名
     * @return 排名前三的用户信息
     */
    public List<LazyInfo> getLazyboard(Integer timeType);

    /**
     * 用于获取控制台所需的快递数据,(包含总快递数和未取快递数,size:总数,day:当日新增)
     * @return [{size:1000,day:100},{size:500,day:100}]
     */
    public List<Map<String, Integer>> console();

    /**
     * 快件列表查询(分页查询)
     * @param limit true表示开启分页(默认),false表示查询所有
     * @param offset SQL语句查询起始索引
     * @param pageNumber 查询的数量
     * @return 查询到的快递信息列表
     */
    public List<Express> findAll(boolean limit, int offset, int pageNumber);

    /**
     * 根据快递单号查询快递信息
     * @param number 快递单号
     * @return 查询到的快递信息
     */
    public Express findByNumber(String number);

    /**
     * 根据id快递信息
     * @param id 快递id
     * @return 查询到的快递信息
     */
    public Express findById(Integer id);

    /**
     * 根据邮箱查询快递信息
     * @param email 快递单号
     * @return 查询到的快递信息
     */
    public List<Express> findByEmail(String email);

    /**
     * 根据取件码查询快递信息
     * @param code 取件码
     * @return 查询到的快递信息
     */
    public Express findByCode(String code);

    /**
     * 根据用户手机号查询用户所有快递信息
     * @param userPhone 用户手机号
     * @param status 0表示查询待取件的快递(默认)
     *               1表示查询已取件的快递
     *               2表示查询用户所有快递
     * @return 查询到的快递信息列表
     */
    public List<Express> findByUserPhone(String userPhone, int status);

    /**
     * 根据录入人手机号查询快递信息
     * @param sysPhone 录入人手机号
     * @return 查询到的快递信息列表
     */
    public List<Express> findBySysPhone(String sysPhone);

    /**
     * 快递录入
     * @param e 封装好的快递信息
     * @return true表示录入成功, false表示录入失败
     */
    public boolean insert(Express e) throws DuplicateCodeException, DuplicateNumberException;

    /**
     * 根据id修改快递出信息
     * @param id 要修改的快递id
     * @param newExpress 新的快递信息
     * @return true表示修改成功, false表示修改失败
     */
    public boolean update(int id, Express newExpress) throws DuplicateNumberException, DuplicateNumberException;

    /**
     * 根据id删除快递信息
     * @param id 要删除的快递id
     * @return true表示删除成功, false表示删除失败
     */
    public boolean delete(int id);

    /**
     * 根据快递单号修改快递为已取件状态
     * @param code 要修改的快递单号
     * @param outTime 出货时间
     * @return true表示修改成功, false表示修改失败
     */
    public boolean updateStatus(String code, Timestamp outTime);



}






