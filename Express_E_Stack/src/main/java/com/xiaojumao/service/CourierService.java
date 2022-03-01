package com.xiaojumao.service;

import com.xiaojumao.bean.Courier;
import com.xiaojumao.dao.imp.CourierDaoMysql;
import com.xiaojumao.exception.DuplicateCNameException;
import com.xiaojumao.exception.DuplicateCPhoneException;
import com.xiaojumao.exception.DuplicateEmailException;
import com.xiaojumao.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 22:25
 * @Modified By:
 */
@Service
public class CourierService implements ICourierService{
    @Autowired
    private CourierDaoMysql dao;

    public CourierDaoMysql getDao() {
        return dao;
    }

    public void setDao(CourierDaoMysql dao) {
        this.dao = dao;
    }

    /**
     * 通过邮箱查询快递员信息
     *
     * @param email by邮箱
     * @return 快递员信息, 查询不到返回null
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Courier findByEmail(String email) {
        return dao.findByEmail(email);
    }

    /**
     * 查询总快递员数和今日新增数量
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<String, Integer> console() {
        return dao.console();
    }

    /**
     * 查询快递员信息列表(分页)
     *
     * @param limit    0分页(默认), 1查询全部
     * @param offset   从该位置开始查询
     * @param pageSize 最多查询的条数
     * @return 快递员信息列表, 否则返回null
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Courier> findAll(boolean limit, int offset, int pageSize) {
        return dao.findAll(limit, offset, pageSize);
    }

    /**
     * 根据手机号查询快递员
     *
     * @param cPhone 快递员
     * @return 查询到的快递员信息, 否则返回null
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Courier findByCPhone(String cPhone) {
        return dao.findByCPhone(cPhone);
    }

    /**
     * 添加快递员
     *
     * @param u 添加的快递员信息
     * @return true:添加成功,false:添加失败
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean add(Courier u) throws DuplicateCNameException, DuplicateCPhoneException {
        // 生成 sendNum regTime lastTime
        u.setSendNum("0");
        u.setRegTime(DateFormatUtil.dateFormatToTimestamp(new Date()));
        u.setLastTime(DateFormatUtil.dateFormatToTimestamp(new Date()));
        return dao.add(u);
    }

    /**
     * 修改快递员信息
     *
     * @param id   待修改的快递员id
     * @param newc 新快递员的信息
     * @return true:修改成功, false:修改失败
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean update(int id, Courier newc) throws DuplicateCNameException, DuplicateCPhoneException, DuplicateEmailException {
        return dao.update(id, newc);
    }

    /**
     * 修改快递员信息
     * @param email 待修改的快递员邮箱
     * @param newc 新快递员的信息
     * @return true:修改成功, false:修改失败
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean update(String email, Courier newc) throws DuplicateCNameException, DuplicateCPhoneException, DuplicateEmailException{
        return dao.update(email, newc);
    }

    /**
     * 根据快递员id删除快递员信息
     *
     * @param id 待删除的快递员id
     * @return true:删除成功, false:删除失败
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean delete(int id) {
        return dao.delete(id);
    }

    /**
     * 检查邮箱和密码是否匹配
     * @param email : 邮箱
     * @param password : 密码
     * @return 匹配返回快递员信息,失败返回null
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Courier checkEmailAndPassword(String email, String password){
        return dao.checkEmailAndPassword(email, password);
    }
}
