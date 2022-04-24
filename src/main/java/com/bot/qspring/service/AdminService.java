package com.bot.qspring.service;

import com.bot.qspring.entity.*;
import com.bot.qspring.mapper.*;
import com.bot.qspring.model.Vo.MessageVo;
import com.bot.qspring.service.dbauto.AchieveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private AchievementMapper achievementMapper;

    @Autowired
    private AchieveRecordService achieveRecordService;

    @Autowired
    private AchieveRecordMapper achieveRecordMapper;

    @Autowired
    private SenderService senderService;

    @Autowired
    private ReplacewordService replacewordService;

    @Autowired
    private ReplacewordMapper replacewordMapper;

    public boolean isAdmin(Long id){
        Admin admin = adminMapper.selectById(id);
        return admin != null;
    }

    public String handleAdminAll(MessageVo vo){
        if(!isAdmin(vo.getUser_id())){
            return "您没有管理员权限";
        }
        String[] msgList = vo.getMessage().split(" ");
        String cmd = msgList[1];

        for(int i = 2 ; i < msgList.length ; i++){
            String raw = msgList[i];
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("word_before", raw);
            List<Replaceword> replaces = replacewordMapper.selectByMap(queryMap);
            //has After
            if(replaces.size() != 0){
                msgList[i] = replaces.get(0).getWordAfter();
            }
        }
        switch (cmd){
            case "give":
                if(msgList.length == 5){
                    return giveAchievement(Long.valueOf(msgList[2]), Long.valueOf(msgList[3]), msgList[4]);
                }
                else if(msgList.length == 6){
                    return giveAchievement(Long.valueOf(msgList[2]), Long.valueOf(msgList[3]), msgList[4], msgList[5]);
                }
                else{
                    return "参数数量错误";
                }
            case "加签":

        }
        return "ERROR 为啥会返回这句呢";
    }

    @Autowired
    private DivinationMapper divinationMapper;

    //TODO 写完手动加签
//    public String addDivination(String Type, String Title, String Detail){
//        Divination divination = new Divination();
//        Map<String, Object> queryMap = new HashMap<>();
//        queryMap.put("type", Type);
//        List<Divination> divinations = divinationMapper.selectByMap(queryMap);
//
//    }

    public String giveAchievement(Long GroupId, Long UserId, String achName){
        return giveAchievement(GroupId, UserId, achName, "");
    }

    public String giveAchievement(Long GroupId, Long UserId, String achName, String Detail){
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("achieve_name", achName);
        List<Achievement> achievements = achievementMapper.selectByMap(queryMap);
        if(achievements.size() == 0){
            //没有成就则增加成就
            if(Detail.equals("")){
                return "新建成就需要给Detail";
            }
            Achievement achievement = new Achievement();
            achievement.setAchieveName(achName);
            achievement.setDetail(Detail);
            achievement.setIsCustom(true);
            achievementMapper.insert(achievement);
            achievements = achievementMapper.selectByMap(queryMap);
        }
        Achievement achievement = achievements.get(0);
        String wonString = achievementService.wonAchieve(achievement.getId(), UserId, GroupId);
        if(wonString.equals("")){
            return "该用户似乎已经获得过该成就，不能重复发放";
        }
        else{
            wonString = "获得成就：\n" + wonString;
            senderService.sendGroup(UserId, GroupId, wonString);
            return "成就信息已发送";
        }

    }

}
