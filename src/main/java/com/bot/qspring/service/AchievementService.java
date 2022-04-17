package com.bot.qspring.service;

import com.bot.qspring.entity.AchieveRecord;
import com.bot.qspring.entity.Achievement;
import com.bot.qspring.mapper.AchieveRecordMapper;
import com.bot.qspring.mapper.AchievementMapper;
import com.bot.qspring.model.Vo.MessageVo;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AchievementService {
    @Autowired
    AchievementMapper achievementMapper;

    @Autowired
    AchieveRecordMapper achieveRecordMapper;

    @Autowired
    WordCounterService wordCounterService;

    @Autowired
    SenderService senderService;

    public String checkTime(MessageVo vo){
        StringBuilder builder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        switch (now.getHour()){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                builder.append(wonAchieve(14L, vo.getUser_id(),vo.getGroup_id()));
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                builder.append(wonAchieve(15L, vo.getUser_id(),vo.getGroup_id()));
                break;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                builder.append(wonAchieve(16L, vo.getUser_id(),vo.getGroup_id()));
                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                builder.append(wonAchieve(17L, vo.getUser_id(),vo.getGroup_id()));
                break;
        }
        Map<String, Object> map = new HashMap<>();
        for(long i = 14L; i <= 17; i += 1){
            map.put("achievement_id", i);
            if(achieveRecordMapper.selectByMap(map).size() == 0){
                return builder.toString();
            }
        }
        builder.append(wonAchieve(18L, vo.getUser_id(), vo.getGroup_id()));
        return builder.toString();
    }

    public String getAllAchieve(Long userId){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        List<AchieveRecord> achieveRecords = achieveRecordMapper.selectByMap(map);
        if(achieveRecords.size() == 0){
            return "啊哦，您好像没有成就诶，来试试求个签吧~";
        }
        else{
            StringBuilder builder = new StringBuilder();
            builder
                    .append("【您的成就】")
                    .append("\n您共有【").append(achieveRecords.size())
                    .append("】项成就");
            achieveRecords.sort(Comparator.comparing(AchieveRecord::getWonDate));
            LocalDate date = null;
            for(AchieveRecord record : achieveRecords){
                if(date == null || !date.equals(record.getWonDate())){
                    date = record.getWonDate();
                    builder.append("\n————————\n").append(date).append("获得：");
                }
                Achievement achievement = achievementMapper.selectById(record.getAchievementId());
                builder
                        .append("\n【")
                        .append(achievement.getAchieveName())
                        .append("】")
                        .append(achievement.getDetail());
            }
            return builder.toString();
        }
    }

    private String getCongratulations(Long achieveId, Long userId, Long groupId){
        StringBuilder builder = new StringBuilder();
        builder.append("获得成就:");
        Achievement achievement = achievementMapper.selectById(achieveId);
        builder
                .append("【")
                .append(achievement.getAchieveName())
                .append("】\n")
                .append(achievement.getDetail())
                .append("\n");
        //senderService.sendGroup(userId, groupId, builder.toString());
        return builder.toString();
    }

    public String getFirstCongratulation(Long achieveId, Long userId, Long groupId){
        StringBuilder builder = new StringBuilder();
        Achievement achievement = achievementMapper.selectById(achieveId);
        builder
                .append("你是")
                .append("【")
                .append(achievement.getAchieveName())
                .append("】")
                .append("成就的首位获得者！\n");
        return builder.toString();
    }

    public String wonAchieve(Long achieveId, Long userId, Long groupId){
        Achievement achievement = achievementMapper.selectById(achieveId);
        StringBuilder builder = new StringBuilder();
        if(achievement.getFirstWon() == null){
            achievement.setFirstWon(userId);
            achievementMapper.updateById(achievement);
            builder.append(getFirstCongratulation(achieveId, userId, groupId));
            builder.append(wonAchieve(8L, userId, groupId));    //凡是第一个获得某个成就都会获得南波湾成就
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("achievement_id",achieveId);
        List<AchieveRecord> achieveRecords = achieveRecordMapper.selectByMap(map);
        if(achieveRecords.size() == 0){
            AchieveRecord achieveRecord = new AchieveRecord();
            achieveRecord.setUserId(userId);
            achieveRecord.setAchievementId(Math.toIntExact(achieveId));
            achieveRecord.setWonDate(LocalDate.now());
            achieveRecordMapper.insert(achieveRecord);
            builder.append(getCongratulations(achieveId,userId,groupId));
        }
        return builder.toString();
    }

}
