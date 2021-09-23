package com.xiaojumao.dao;

import com.xiaojumao.bean.Courier;
import com.xiaojumao.exception.DuplicateCNameException;
import com.xiaojumao.exception.DuplicateCPhoneException;
import com.xiaojumao.exception.DuplicateEmailException;

import java.util.List;
import java.util.Map;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 16:57
 * @Modified By:
 */
public interface BaseCourierDao {

    /**
     * 通过邮箱查询快递员信息
     * @param email by邮箱
     * @return 快递员信息,查询不到返回null
     */
    public Courier findByEmail(String email);

    /**
     * 查询总快递员数和今日新增数量
     * @return
     */
    public Map<String, Integer> console();

    /**
     * 查询快递员信息列表(分页)
     * @param limit 0分页(默认), 1查询全部
     * @param offset 从该位置开始查询
     * @param pageSize 最多查询的条数
     * @return 快递员信息列表, 否则返回null
     */
    public List<Courier> findAll(boolean limit, int offset, int pageSize);

    /**
     * 根据手机号查询快递员
     * @param cPhone 快递员
     * @return 查询到的快递员信息, 否则返回null
     */
    public Courier findByCPhone(String cPhone);

    /**
     * 添加快递员
     * @param u 添加的快递员信息
     * @return true:添加成功,false:添加失败
     */
    public boolean add(Courier u) throws DuplicateCNameException, DuplicateCPhoneException;

    /**
     * 修改快递员信息
     * @param id 待修改的快递员id
     * @param newc 新快递员的信息
     * @return true:修改成功, false:修改失败
     */
    public boolean update(int id, Courier newc) throws DuplicateCNameException, DuplicateCPhoneException, DuplicateEmailException;

    /**
     * 修改快递员信息
     * @param email 待修改的快递员邮箱
     * @param newc 新快递员的信息
     * @return true:修改成功, false:修改失败
     */
    public boolean update(String email, Courier newc) throws DuplicateCNameException, DuplicateCPhoneException, DuplicateEmailException;

    /**
     * 根据快递员id删除快递员信息
     * @param id 待删除的快递员id
     * @return true:删除成功, false:删除失败
     */
    public boolean delete(int id);

    /**
     * 检查邮箱和密码是否匹配
     * @param email : 邮箱
     * @param password : 密码
     * @return 匹配返回快递员信息,失败返回null
     */
    public Courier checkEmailAndPassword(String email, String password);
}
