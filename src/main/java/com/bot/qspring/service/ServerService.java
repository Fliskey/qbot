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
import java.util.*;

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


    private String getGuiding(MessageVo vo){
        StringBuilder builder = new StringBuilder();
        builder
                .append("使用指令集：\n")
                .append("【求签】求取一条签文\n")
                .append("【求签统计】今日群运势统计\n")
                .append("【求签排名】今日求签前五并at\n")
                .append("【成就】查看本人已获成就\n")
                .append("【bot github】显示bot的Github源码，一条消息中含两个关键词则触发\n")
                .append("【bot help】显示此段指令集\n")
        ;
        if(vo.getGroup_id().equals(438851137L)){
            builder
                    .append("【报名 活动名】报名一项活动，活动不存在则新建\n")
                    .append("【取消报名 活动名】取消一项活动的报名\n")
                    .append("【报名统计 活动名】显示该活动的报名信息\n")
            ;
        }
        return builder.toString();
    }

    private String getPokeRet(){
        String ret = "";
        Random random = new Random();
        int getInt = random.nextInt(5);
        switch (getInt){
            case 0:
                ret = "啊啊，再戳就要宕机了";
                break;
            case 1:
                ret = "我有那么好戳吗？=v=";
                break;
            case 2:
                ret = "针不戳！";
                break;
            case 3:
                ret = "好痒啊，别戳了啦";
                break;
            case 4:
                ret = "戳我何事？";
        }
        return ret;
    }


    public void handleAll(MessageVo messageVo){
        switch (messageVo.getPost_type()){
            case "meta_event":
                break;
            case "notice":
                switch(messageVo.getSub_type()){
                    case "poke":
                        //拍一拍
                        if(messageVo.getSelf_id().equals(messageVo.getTarget_id())){
                            String ret = achievementService.wonAchieve(7L, messageVo.getUser_id(), messageVo.getGroup_id());
                            if(!ret.equals("")){
                                senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), ret);
                            }
                            else{
                                ret = getPokeRet();
                                senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), ret);
                            }
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
            builder.append(achievementService.wonAchieve(2L, messageVo.getUser_id(), messageVo.getGroup_id()));
            if(!builder.toString().equals("")){
                builder = new StringBuilder()
                        .append("获得成就：\n")
                        .append(builder);
                senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());
            }
            builder.append("发起者：").append(messageVo.getSender()).append("\n");
            builder.append("签文：").append(msg);
            senderService.sendPrivate(2214106974L, builder.toString());
            return;
        }
        else if(msg.toLowerCase().contains("help")){
            if(msg.toLowerCase().contains("bot")){
                builder.append(getGuiding(messageVo));
            }
            else if(msg.toLowerCase().contains("github")){
                builder.append("https://github.com/Fliskey/qbot");
            }
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
                String ret = wordCounterService.checkWordCounter(messageVo);
                if (!ret.equals("YES")) {
                    builder.append(ret).append("\n");
                    String achieve = achievementService.wonAchieve(1L, messageVo.getUser_id(), messageVo.getGroup_id());
                    if(!achieve.equals("")){
                        builder
                                .append("\n获得成就：\n")
                                .append(achieve);
                    }
                    senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());
                    return;
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
                builder.append(appDivinationService.beginDivination(messageVo));
                break;
            }
            default:{
                return;
            }
        }
        senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());
    }


}
