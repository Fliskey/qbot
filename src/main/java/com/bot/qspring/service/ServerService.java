package com.bot.qspring.service;

import com.bot.qspring.entity.Customreply;
import com.bot.qspring.mapper.CustomreplyMapper;
import com.bot.qspring.model.Vo.MessageVo;
import com.bot.qspring.service.stopped.AppPartyService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ServerService {

    @Autowired
    private SenderService senderService;

    @Autowired
    private AppDivinationService appDivinationService;

    @Autowired
    private AppPartyService appPartyService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private WordCounterService wordCounterService;


    public void handleAll(MessageVo messageVo){
        switch (messageVo.getPost_type()){
            case "meta_event":
                break;
            case "notice":
                switch(messageVo.getSub_type()){
                    case "poke":
                        //拍一拍
                        if(messageVo.getSelf_id().equals(messageVo.getTarget_id())){
                            achievementService.wonAchieve(7L, messageVo.getUser_id(), messageVo.getGroup_id());
                        }
                        break;
                    case "approve":
                        //进群
                        break;
                    case "kick":
                        //踢人
                        break;
                }
                break;
            case "message":
                switch (messageVo.getMessage_type()){
                    case "private":
                        handlePrivate(messageVo);
                        break;
                    case "group":
                        handleGroup(messageVo);
                        break;
                }
                break;
        }
    }

    public void handlePrivate(MessageVo messageVo){

    }


    public void handleGroup(MessageVo messageVo){
        String msg = messageVo.getMessage();
        System.out.println(msg);

        StringBuilder builder = new StringBuilder();
        //部分匹配
        if(msg.contains("【宜】") || msg.contains("【忌】")){
            achievementService.wonAchieve(2L, messageVo.getUser_id(), messageVo.getGroup_id());
            builder.append("发起者：").append(messageVo.getSender()).append("\n");
            builder.append("签文：").append(msg);
            senderService.sendPrivate(2214106974L, builder.toString());
            return;
        }
        else if(msg.startsWith("报名统计")){
            builder.append(appPartyService.staticsParty(messageVo));
        }
        else if(msg.startsWith("报名")){
            builder.append(appPartyService.signUpParty(messageVo));
        }
        else if(msg.startsWith("取消报名")){
            builder.append(appPartyService.cancelSignUpParty(messageVo));
        }

        //全词匹配前确认Builder是否有需要发送的内容
        if(!builder.toString().equals("")){
            wordCounterService.checkWordCounter(messageVo);
            senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());
            return;
        }
        //全词匹配

        switch (msg){
            case "求签统计":
            case "求签排名":
            case "求签":
            case "成就":
                switch (wordCounterService.checkWordCounter(messageVo)){
                    case -1:
                        achievementService.wonAchieve(1L, messageVo.getUser_id(), messageVo.getGroup_id());
                        return;
                    case 0:
                        achievementService.wonAchieve(10L, messageVo.getUser_id(), messageVo.getGroup_id());
                        return;
                    default:
                }
        }
        switch (msg) {
            case "成就": {
                builder.append(achievementService.getAllAchieve(messageVo.getUser_id()));
                break;
            }
            case "求签统计": {
                builder.append(appDivinationService.groupStatics(messageVo));
                break;
            }
            case "求签排名": {
                builder.append(appDivinationService.groupRanking(messageVo));
                break;
            }
            case "求签": {
                achievementService.wonAchieve(9L, messageVo.getUser_id(), messageVo.getGroup_id());
                builder.append(appDivinationService.beginDivination(messageVo));
                break;
            }
            default:{
                return;
            }
        }
        achievementService.checkTime(messageVo);
        senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());

    }


}
