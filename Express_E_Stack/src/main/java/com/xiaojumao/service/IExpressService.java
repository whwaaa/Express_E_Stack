package com.xiaojumao.service;

import com.xiaojumao.bean.Express;
import com.xiaojumao.bean.LazyInfo;
import com.xiaojumao.bean.Message;
import com.xiaojumao.exception.DuplicateNumberException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface IExpressService {
    /**
     * 获取快递数量排名前三的用户信息
     *
     * @param timeType 0:总排名 1:年排名 2:月排名
     * @return 排名前三的用户信息
     */
    List<LazyInfo> getLazyboard(Integer timeType);

    /**
     * 用于获取控制台所需的快递数据,(包含总快递数和未取快递数,size:总数,day:当日新增)
     *
     * @return [{size:1000,day:100},{size:500,day:100}]
     */
    List<Map<String, Integer>> console();

    /**
     * 快件列表查询(分页查询)
     *
     * @param limit      true表示开启分页(默认),false表示查询所有
     * @param offset     SQL语句查询起始索引
     * @param pageNumber 查询的数量
     * @return 查询到的快递信息列表
     */
    List<Express> findAll(boolean limit, int offset, int pageNumber);

    /**
     * 根据快递单号查询快递信息
     *
     * @param number 快递单号
     * @return 查询到的快递信息
     */
    Express findByNumber(String number);

    /**
     * 根据邮箱查询快递信息
     *
     * @param email 快递单号
     * @return 查询到的快递信息
     */
    List<Express> findByEmail(String email);

    /**
     * 根据取件码查询快递信息
     *
     * @param code 取件码
     * @return 查询到的快递信息
     */
    Express findByCode(String code);

    /**
     * 根据用户手机号查询用户所有快递信息
     *
     * @param userPhone 用户手机号
     * @param status    0表示查询待取件的快递(默认)
     *                  1表示查询已取件的快递
     *                  2表示查询用户所有快递
     * @return 查询到的快递信息列表
     */
    List<Express> findByUserPhone(String userPhone, int status);

    /**
     * 根据录入人手机号查询快递信息
     *
     * @param sysPhone 录入人手机号
     * @return 查询到的快递信息列表
     */
    List<Express> findBySysPhone(String sysPhone);

    /**
     * 快递录入
     *
     * @param e 封装好的快递信息
     * @return 录入成功返回取件码
     */
    String insert(Express e) throws DuplicateNumberException;

    /**
     * 根据id修改快递信息
     *
     * @param id         要修改的快递id
     * @param newExpress 新的快递信息
     * @return true表示修改成功, false表示修改失败
     */
    boolean update(int id, Express newExpress);

    /**
     * 根据id删除快递信息
     *
     * @param id 要删除的快递id
     * @return true表示删除成功, false表示删除失败
     */
    boolean delete(int id);

    /**
     * 根据快递单号修改快递为已取件状态
     *
     * @param code 要修改的快递单号
     * @param outTime 出货时间
     * @return true表示修改成功, false表示修改失败
     */
    boolean updateStatus(String code, Timestamp outTime);

    /**
     * 根据id查询快递信息
     *
     * @param id 快递id
     * @return 查询到的快递信息
     */
    Express findById(Integer id);
}
