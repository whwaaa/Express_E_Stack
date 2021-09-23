package com.xiaojumao.util;

import java.util.Random;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-01 16:17
 * @Modified By:
 */
public class RandomUtil {
    private static Random random = new Random();

    /**
     * 随机生成6位数
     * @return 6位随机取件码
     */
    public static String getCode(){
        int i = random.nextInt(900000)+100000;
        return String.valueOf(i);
    }
}
