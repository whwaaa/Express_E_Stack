package com.xiaojumao.service;

import com.xiaojumao.bean.User;
import com.xiaojumao.exception.DuplicateEmailException;
import com.xiaojumao.exception.DuplicateUserNameException;
import com.xiaojumao.exception.DuplicateUserPhoneException;

import java.util.List;
import java.util.Map;

public interface IUserService {
    /**
     * 查询总快递员数和今日新增数量
     *
     * @return
     */
    Map<String, Integer> console();

    /**
     * 通过邮箱查询用户信息
     *
     * @param email by邮箱
     * @return 用户信息, 查询不到返回null
     */
    User findByEmail(String email);

    /**
     * 查询用户信息列表(分页)
     *
     * @param limit    0分页(默认), 1查询全部
     * @param offset   从该位置开始查询
     * @param pageSize 最多查询的条数
     * @return 用户信息列表, 否则返回null
     */
    List<User> findAll(boolean limit, int offset, int pageSize);

    /**
     * 根据手机号查询用户
     *
     * @param userPhone 用户
     * @return 查询到的用户信息, 否则返回null
     */
    User findByUserPhone(String userPhone);

    /**
     * 添加用户
     *
     * @param u 添加的用户信息
     * @return true:添加成功,false:添加失败
     */
    boolean add(User u) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateEmailException;

    /**
     * 修改用户信息
     *
     * @param id   待修改的用户id
     * @param newu 新用户的信息
     * @return true:修改成功, false:修改失败
     */
    boolean update(int id, User newu) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateEmailException;

    /**
     * 修改用户信息
     * @param email 待修改的用户邮箱
     * @param newu 新用户的信息
     * @return true:修改成功, false:修改失败
     */
    boolean update(String email, User newu) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateEmailException;

    /**
     * 根据用户id删除用户信息
     *
     * @param id 待删除的用户id
     * @return true:删除成功, false:删除失败
     */
    boolean delete(int id);

    /**
     * 检查邮箱和密码是否匹配
     *
     * @param email    : 邮箱
     * @param password : 密码
     * @return 匹配返回用户信息, 失败返回null
     */
    User checkEmailAndPassword(String email, String password);
}
