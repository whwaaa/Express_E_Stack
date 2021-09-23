package com.xiaojumao.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-03 12:33
 * @Modified By:
 */
public class BootStrapResultData<T> {
    // 查询到的数据集合
    private List<T> rows = new ArrayList<T>();
    // 总数
    private int total;

    public BootStrapResultData() {
    }

    public BootStrapResultData(List<T> rows, int total) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
