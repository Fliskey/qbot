package com.bot.qspring.controller;

import com.bot.qspring.model.Vo.MessageVo;
import com.bot.qspring.service.AppDivinationService;
import com.bot.qspring.service.ServerService;
import com.bot.qspring.service.WordCounterService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static java.lang.Thread.sleep;

@RestController
public class ServerController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private AppDivinationService appDivinationService;

    @PostMapping("/")
    public Object receiveAll(@RequestBody String body){
        Gson gson = new Gson();
        MessageVo messageVo = gson.fromJson(body, MessageVo.class);
        serverService.handleAll(messageVo);
        return new Object();
    }

}
