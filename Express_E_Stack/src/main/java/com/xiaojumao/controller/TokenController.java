package com.xiaojumao.controller;

import com.xiaojumao.bean.Result;
import com.xiaojumao.service.RedisService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * CORSTest
 *
 * @author wuhanwei
 * @version 1.0
 * @date 2021/9/22
 */

public class TokenController {

    private RedisService redisService;

//    @ResponseBody
//    @RequestMapping(value = "initLogin", method = RequestMethod.GET)
    public Result initLogin(){
        String token = UUID.randomUUID().toString().replace("-", "");
//        System.out.println("生成的token: " + token);
        long res = redisService.sSetAndTime("token", 1800, token);
        if(res > 0)
            return new Result(200, "ok", token);
        else
            return new Result(500, "faild", null);
    }

//    @ResponseBody
//    @RequestMapping(value = "set", method = RequestMethod.POST)
    public Result set(@RequestParam("token") String token){
//        System.out.println("登陆验证的token: " + token);
        boolean token1 = redisService.sHasKey("token", token);
        if(token1)
            return new Result(200, "ok", null);
        else
            return new Result(200, "faild", null);
    }
}
