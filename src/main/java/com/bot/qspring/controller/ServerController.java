package com.bot.qspring.controller;

import com.bot.qspring.entity.vo.MessageVo;
import com.bot.qspring.service.AppDivinationService;
import com.bot.qspring.service.ServerService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private AppDivinationService appDivinationService;

    @PostMapping("/")
    public Object receiveAll(@RequestBody String body) {
        Gson gson = new Gson();
        MessageVo messageVo = gson.fromJson(body, MessageVo.class);
        serverService.handleAll(messageVo);
        return new Object();
    }

    @PostMapping("/sendVerifyCode")
    public Object sendVerifyCode(@RequestParam String qq, @RequestParam String code) {
        serverService.sendVerifyCode(qq, code);
        return new Object();
    }

}
