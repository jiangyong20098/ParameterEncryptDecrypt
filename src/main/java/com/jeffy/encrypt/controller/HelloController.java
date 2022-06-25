package com.jeffy.encrypt.controller;

import com.jeffy.encrypt.domain.User;
import com.jeffy.encrypt.starter.anno.Decrypt;
import com.jeffy.encrypt.starter.anno.Encrypt;
import com.jeffy.encrypt.starter.utils.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    /**
     * GET http://localhost:8080/user
     * Accept: application/json
     *
     * {
     *   "status": 200,
     *   "msg": "hjLGnhKa2f6OGGOwHUYNtA==",
     *   "obj": "P3whrQbA+9uyfnT2mD6iXcxBcAzu3nn8fkLrhegIldyO2Xkm6TY6fnrocAf/4f5L"
     * }
     * @return
     */
    @GetMapping("/user")
    @Encrypt
    public RespBean getUser() {
        User user = new User();
        user.setId((long) 99);
        user.setUsername("hello jeffy");
        return RespBean.ok("ok", user);
    }

    /**
     * postman POST http://localhost:8080/user
     * Body -> raw -> JSON : P3whrQbA+9uyfnT2mD6iXcxBcAzu3nn8fkLrhegIldyO2Xkm6TY6fnrocAf/4f5L
     *
     * {
     *     "status": 200,
     *     "msg": "ok",
     *     "obj": {
     *         "id": 99,
     *         "username": "hello jeffy"
     *     }
     * }
     * @param user
     * @return
     */
    @PostMapping("/user")
    public RespBean addUser(@RequestBody @Decrypt User user) {
        System.out.println("user = " + user);
        return RespBean.ok("ok", user);
    }
}