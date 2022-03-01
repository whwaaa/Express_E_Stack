package com.xiaojumao.service;

//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import com.xiaojumao.bean.Express;
import com.xiaojumao.bean.LazyInfo;
import com.xiaojumao.dao.imp.ExpressDaoMysql;
import com.xiaojumao.exception.DuplicateNumberException;
import com.xiaojumao.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-01 16:09
 * @Modified By:
 */
@Service
public class ExpressService implements IExpressService {
    @Autowired
    private  ExpressDaoMysql dao;

    /**
     * 获取快递数量排名前三的用户信息
     *
     * @param timeType 0:总排名 1:年排名 2:月排名
     * @return 排名前三的用户信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LazyInfo> getLazyboard(Integer timeType) {
        return dao.getLazyboard(timeType);
    }

    /**
     * 用于获取控制台所需的快递数据,(包含总快递数和未取快递数,size:总数,day:当日新增)
     *
     * @return [{size:1000,day:100},{size:500,day:100}]
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Map<String, Integer>> console() {
        return dao.console();
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
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Express> findAll(boolean limit, int offset, int pageNumber) {
        return dao.findAll(limit, offset, pageNumber);
    }

    /**
     * 根据快递单号查询快递信息
     *
     * @param number 快递单号
     * @return 查询到的快递信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Express findByNumber(String number) {
        return dao.findByNumber(number);
    }

    /**
     * 根据邮箱查询快递信息
     *
     * @param email 快递单号
     * @return 查询到的快递信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Express> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    /**
     * 根据取件码查询快递信息
     *
     * @param code 取件码
     * @return 查询到的快递信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Express findByCode(String code) {
        return dao.findByCode(code);
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
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Express> findByUserPhone(String userPhone, int status) {
        return dao.findByUserPhone(userPhone, status);
    }

    /**
     * 根据录入人手机号查询快递信息
     *
     * @param sysPhone 录入人手机号
     * @return 查询到的快递信息列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Express> findBySysPhone(String sysPhone) {
        return dao.findBySysPhone(sysPhone);
    }

    /**
     * 快递录入
     *
     * @param e 封装好的快递信息
     * @return 录入成功返回取件码
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public String insert(Express e) throws DuplicateNumberException {
        // 1.生成取件码
        String code = RandomUtil.getCode();
        e.setCode(code);
        // 2.录入数据
        try {
            if(dao.insert(e))
                return code;
        } catch (DuplicateKeyException duplicateKeyException){
            String message = duplicateKeyException.getMessage();
            String codeDuplicate = ".*Duplicate entry.*for key 'express.code'.*";
            String numberDuplicate = ".*Duplicate entry.*for key 'express.number'.*";
            if(Pattern.matches(codeDuplicate, message)){
                // 取件码重复异常,递归重新录入
                return insert(e);
            }else if(Pattern.matches(numberDuplicate, message)){
                // 单号重复,抛出异常
                throw new DuplicateNumberException();
            }
        }
        return null;
    }

    /**
     * 根据id修改快递信息
     *
     * @param id         要修改的快递id
     * @param newExpress 新的快递信息
     * @return true表示修改成功, false表示修改失败
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean update(int id, Express newExpress) {
        try {
            return dao.update(id, newExpress);
//            return new Message(0, "修改成功");
        } catch (DuplicateKeyException e) {
            // 快递单号重复异常
            e.printStackTrace();
//            return new Message(-1, "快递单号重复");
            return false;
        }
    }

    /**
     * 根据id删除快递信息
     *
     * @param id 要删除的快递id
     * @return true表示删除成功, false表示删除失败
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean delete(int id) {
        return dao.delete(id);
    }

    /**
     * 根据快递单号修改快递为已取件状态
     *
     * @param code 要修改的快递单号
     * @param outTime 出货时间
     * @return true表示修改成功, false表示修改失败
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean updateStatus(String code, Timestamp outTime) {
        return dao.updateStatus(code, outTime);
    }

    /**
     * 根据id查询快递信息
     *
     * @param id 快递id
     * @return 查询到的快递信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Express findById(Integer id) {
        return dao.findById(id);
    }
}
