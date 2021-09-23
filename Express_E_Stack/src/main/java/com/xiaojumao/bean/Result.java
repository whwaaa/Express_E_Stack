package com.xiaojumao.bean;

import java.util.List;

/**
 * springProject
 *
 * @author wuhanwei
 * @version 1.0
 * @date 2021/9/18
 */
public class Result<T> {
    private Integer code;
    private String message;
    private T obj;
    private List<T> list;

    public Result() {
        this.code = 200;
        this.message = "ok";
        this.obj = null;
        this.list = null;
    }

    public Result(Integer code, String message, List<T> list) {
        this.code = code;
        this.message = message;
        this.list = list;
    }

    public Result(Integer code, String message, T obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
