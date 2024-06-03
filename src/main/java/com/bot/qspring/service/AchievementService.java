package com.bot.qspring.service;

import com.bot.qspring.dao.AchievementDao;
import com.bot.qspring.entity.bo.AchievementAll;
import com.bot.qspring.entity.po.AchieveRecord;
import com.bot.qspring.entity.po.Achievement;
import com.bot.qspring.entity.vo.MessageVo;
import com.bot.qspring.mapper.AchieveRecordMapper;
import com.bot.qspring.mapper.AchievementMapper;
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

    @Autowired
    AchievementDao achievementDao;

    public String checkTime(MessageVo vo) {
        StringBuilder builder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        switch (now.getHour()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                builder.append(wonAchieve(14L, vo.getUser_id(), vo.getGroup_id()));
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                builder.append(wonAchieve(15L, vo.getUser_id(), vo.getGroup_id()));
                break;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                builder.append(wonAchieve(16L, vo.getUser_id(), vo.getGroup_id()));
                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                builder.append(wonAchieve(17L, vo.getUser_id(), vo.getGroup_id()));
                break;
        }
        Map<String, Object> map = new HashMap<>();
        for (Long i = 14L; i <= 17L; i = i + 1L) {
            map.clear();
            map.put("achievement_id", i);
            map.put("user_id", vo.getUser_id());
            if (achieveRecordMapper.selectByMap(map).size() == 0) {
                return builder.toString();
            }
        }
        builder.append(wonAchieve(18L, vo.getUser_id(), vo.getGroup_id()));
        return builder.toString();
    }

    public String getNotWonAchieve(Long userId) {
        List<AchievementAll> notWonAchieves = achievementDao.getNotWon(userId);
        StringBuilder builder = new StringBuilder();
        if (notWonAchieves.size() == 0) {
            return "截至今日，您已取得所有成就！";
        }
        builder
                .append("您未获得【")
                .append(notWonAchieves.size())
                .append("】项成就：\n");

        int state = 0;
        for (var achieve : notWonAchieves) {
            if (achieve.getIsCustom() != null && state != 1) {
                state = 1;
                builder.append("---- 需人工授予 ----\n");
            } else if (achieve.getIsCustom() == null && state != 2) {
                state = 2;
                builder.append("---- 自动授予 ----\n");
            }
            builder
                    .append(achieve.getCn())
                    .append("【")
                    .append(achieve.getAchieveName())
                    .append("】")
                    .append(achieve.getCn() == 0 ? "★机密" : achieve.getDetail())
                    .append("\n");
        }
        return builder.toString();
    }

    public String getAllAchievementPublic() {
        List<AchievementAll> allAchieve = achievementDao.getAllOwnedAchievement();
        return allAchieveToMsg(allAchieve);
    }

    private String allAchieveToMsg(List<AchievementAll> allAchieve) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("成就图鉴：\n")
                .append("(数字为获得人数)\n");
        for (var achieve : allAchieve) {
            builder
                    .append(achieve.getCn())
                    .append("【")
                    .append(achieve.getAchieveName())
                    .append("】")
                    .append(achieve.getCn() == 0 ? "★机密" : achieve.getDetail())
                    .append("\n");
        }
        return builder.toString();
    }

    public String getAllAchievement(Long groupId) {
        List<AchievementAll> allAchieve = achievementDao.getAllAchieveByGroup(groupId);
        //List<AchievementAll> allAchieve = achievementDao.getAllOwnedAchievement();
        return allAchieveToMsg(allAchieve);
    }


    public String getOnesAchieve(Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        List<AchieveRecord> achieveRecords = achieveRecordMapper.selectByMap(map);
        if (achieveRecords.size() == 0) {
            return "啊哦，您好像没有成就诶，来试试求个签吧~";
        } else {
            StringBuilder builder = new StringBuilder();
            builder
                    .append("【您的成就】")
                    .append("\n您共有【").append(achieveRecords.size())
                    .append("】项成就");
            achieveRecords.sort(Comparator.comparing(AchieveRecord::getWonDate));
            LocalDate date = null;
            for (AchieveRecord record : achieveRecords) {
                if (date == null || !date.equals(record.getWonDate())) {
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

    private String getCongratulations(Long achieveId, Long userId, Long groupId) {
        StringBuilder builder = new StringBuilder();
        Achievement achievement = achievementMapper.selectById(achieveId);
        builder
                .append("【")
                .append(achievement.getAchieveName())
                .append("】")
                .append(achievement.getDetail())
                .append("\n");
        //senderService.sendGroup(userId, groupId, builder.toString());
        return builder.toString();
    }

    public String getFirstCongratulation(Long achieveId, Long userId, Long groupId) {
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

    public String wonAchieve(String achieveName, Long userId, Long groupId) {
        List<Achievement> achievements = achievementMapper.selectByMap(new HashMap<>());
        for (Achievement achievement : achievements) {
            if (achievement.getAchieveName().contains(achieveName)) {
                Long achId = achievement.getId();
                return wonAchieve(achId, userId, groupId);
            }
        }
        return "";
    }

    public String wonAchieve(Long achieveId, Long userId, Long groupId) {
        Achievement achievement = achievementMapper.selectById(achieveId);
        StringBuilder builder = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("achievement_id", achieveId);
        List<AchieveRecord> achieveRecords = achieveRecordMapper.selectByMap(map);
        if (achievement.getFirstWon() == null && achievement.getIsCustom() == null) {
            achievement.setFirstWon(userId);
            achievementMapper.updateById(achievement);
            builder.append(getFirstCongratulation(achieveId, userId, groupId));
            builder.append(wonAchieve(8L, userId, groupId));    //凡是第一个获得某个成就都会获得南波湾成就
        }
        if (achieveRecords.size() == 0) {
            AchieveRecord achieveRecord = new AchieveRecord();
            achieveRecord.setUserId(userId);
            achieveRecord.setAchievementId(Math.toIntExact(achieveId));
            achieveRecord.setWonDate(LocalDate.now());
            achieveRecordMapper.insert(achieveRecord);
            builder.append(getCongratulations(achieveId, userId, groupId));
        } else {
            return "";
        }
        return builder.toString();
    }

}
