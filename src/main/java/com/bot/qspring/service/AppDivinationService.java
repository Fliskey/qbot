package com.bot.qspring.service;

import com.bot.qspring.dao.DivinationDao;
import com.bot.qspring.entity.Divigrouprecord;
import com.bot.qspring.entity.Divination;
import com.bot.qspring.entity.Divirecord;
import com.bot.qspring.mapper.DivirecordMapper;
import com.bot.qspring.model.Vo.MessageVo;
import com.bot.qspring.service.dbauto.DivigrouprecordService;
import com.bot.qspring.service.dbauto.DivirecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AppDivinationService {

    @Autowired
    private DivinationDao divinationDao;

    @Autowired
    private DivirecordService divirecordService;

    @Autowired
    private DivirecordMapper divirecordMapper;

    @Autowired
    private DivigrouprecordService divigrouprecordService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private SenderService senderService;

    public String getMemeFrom(MessageVo vo){
        String msg = vo.getMessage();
        List<Divination> list = divinationDao.getDiviHasMeme();
        for(Divination divi : list){
            String meme = divi.getKey();
            if(msg.contains(meme)){
                String getAch = achievementService.wonAchieve("刨根问底", vo.getUser_id(), vo.getGroup_id());

                StringBuilder builder = new StringBuilder();
                builder.append(divi.getMemeFrom());
                builder.append("\n");

                if(!getAch.equals("")){
                    builder.append("获得成就：\n");
                    builder.append(getAch);
                }

                return builder.toString();
            }
        }
        return "";
    }

    //文字中带【宜】或【忌】
    public String selfGoodBad(MessageVo vo){
        StringBuilder builder = new StringBuilder();
        String msg = vo.getMessage();
        Divirecord divirecord = divirecordService.getById(vo.getUser_id());
        if(!msg.contains("的签文")){
            senderService.sendPrivate(2214106974L, "新签文：\n"+vo.toString());
            builder.append(
                    achievementService.wonAchieve("众人拾柴火焰高", vo.getUser_id(), vo.getGroup_id())
            );
        }
        if(msg.contains("的签文") && msg.contains("【宜】")){
            //自制签文
            if(divirecord != null && divirecord.getLastTime().equals(LocalDate.now())){

                msg = msg.replace("\r","").replace("\n","");
                String diviMsg = divirecord.getLastText();
                diviMsg = diviMsg.replace("\r","").replace("\n","").trim();

                //今天有求过签
                if(msg.contains(diviMsg)){
                    //复读了一遍签文
                    builder.append(
                            achievementService.wonAchieve("人类的本质", vo.getUser_id(), vo.getGroup_id())
                    );
                }
                else if(divirecord.getLastLevel().contains("凶") && msg.contains("吉")){
                    //自己改凶为吉
                    builder.append(
                            achievementService.wonAchieve("我命由我不由天", vo.getUser_id(), vo.getGroup_id())
                    );
                }
                else if(divirecord.getLastLevel().contains("吉") && msg.contains("凶")){
                    //自己改吉为凶
                    builder.append(
                            achievementService.wonAchieve("让暴风雨来得更猛烈些吧！", vo.getUser_id(), vo.getGroup_id())
                    );
                }
                else{
                    builder.append(
                        achievementService.wonAchieve("众人拾柴火焰高", vo.getUser_id(), vo.getGroup_id())
                    );
                }
            }

            //无论今天有没有求过，自己发了一条签文
            builder.append(
                    achievementService.wonAchieve("bot体验卡", vo.getUser_id(), vo.getGroup_id())
            );
        }
        else{
            senderService.sendPrivate(2214106974L, "新签文：\n"+vo.toString());
            builder.append(
                    achievementService.wonAchieve("众人拾柴火焰高", vo.getUser_id(), vo.getGroup_id())
            );
        }
        return builder.toString();
    }

    public String getDiviRecord(MessageVo vo){
        Divirecord divirecord = new Divirecord();
        divirecord = divirecordService.getById(vo.getUser_id());
        if(divirecord == null){
            return "您好像没有求过签，在有bot的群中发送“求签”即可求取签文~";
        }
        else{
            StringBuilder builder = new StringBuilder();
            if(divirecord.getLastTime().equals(LocalDate.now())){
                Long diviGroup = divirecord.getDiviGroup();
                Integer diviGroupNum = divirecord.getRankingNum();
                if(diviGroup == null || diviGroupNum == null){
                    SenderService senderService = new SenderService();
                    senderService.sendPrivate(2214106974L, divirecord.toString());
                    return "啊哦，系统出错了";
                }
                String groupName = senderService.getGroupName(diviGroup);
                builder.append("您今天在【"+groupName+"】群求签\n");
                builder.append("今日签文为：\n");
                builder.append(divirecord.getLastText());
                return builder.toString();
            }
            else{
                return "您今天还没有求签哦\n去任意一个有bot在的群求个签吧~";
            }
        }
    }

    public String groupRanking(MessageVo vo){
        StringBuilder builder = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("last_time", LocalDate.now());
        map.put("divi_group",vo.getGroup_id());
        List<Divirecord> divirecords = divirecordMapper.selectByMap(map);
        builder.append("【今日本群打卡前五】\n");
        divirecords.sort(Comparator.comparing(Divirecord::getDiviTime));
        for(int i = 0; i < 5; i++){
            builder
                    .append("第")
                    .append(i+1)
                    .append("位：");
            if(divirecords.size() <= i){
                builder.append("虚位以待");
            }
            else{
                builder
                        .append("[CQ:at,qq=")
                        .append(divirecords.get(i).getId())
                        .append("]");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String groupStatics(MessageVo vo){
        StringBuilder builder = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("last_time", LocalDate.now());
        map.put("divi_group",vo.getGroup_id());
        List<Divirecord> divirecords = divirecordMapper.selectByMap(map);
        int[] levelNum = new int[5];
        int score = 0;
        String[] levels = new String[]{"大凶", "小凶", "中平", "小吉", "大吉"};
        for(var record : divirecords){
            String level = record.getLastLevel();
            for(int i = 0; i < 5; i++){
                if(level.equals(levels[i])){
                    levelNum[i]++;
                    score += i;
                    break;
                }
            }
        }
        double avgScore = (double)score / (double)divirecords.size();
        builder.append("【本群运势统计】\n");
        for(int i = 0; i < 5; i++){
            builder
                    .append(levels[i])
                    .append("：")
                    .append(levelNum[i])
                    .append("位\n");
        }
        builder.append("平均：");
        DecimalFormat df = new DecimalFormat("0.00");
        builder
                .append(levels[(int) Math.floor(avgScore)])
                .append("+")
                .append(df.format(avgScore - Math.floor(avgScore)))
                .append("\n");

        return builder.toString();
    }

    public String groupBadStatics(MessageVo vo){
        StringBuilder builder = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("last_time", LocalDate.now());
        map.put("divi_group",vo.getGroup_id());
        List<Divirecord> divirecords = divirecordMapper.selectByMap(map);
        int[] levelNum = new int[5];
        int score = 0;
        String[] levels = new String[]{"大凶", "小凶"};
        for(var record : divirecords){
            String level = record.getLastLevel();
            for(int i = 0; i < 2; i++){
                if(level.equals(levels[i])){
                    levelNum[i]++;
                    break;
                }
            }
        }
        builder.append("【本群凶签统计】\n");
        builder.append("共打卡【"+divirecords.size()+"】位\n");
        for(int i = 0; i < 2; i++){
            builder
                    .append(levels[i])
                    .append("：")
                    .append(levelNum[i])
                    .append("位\n");
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String takes = df.format((double)(levelNum[0]+levelNum[1])/(double)divirecords.size()*100);
        builder.append("凶签共占"+takes+"%");
        return builder.toString();
    }

    private String toComplete(String level, String good, String bad){
        StringBuilder builder = new StringBuilder();
        builder.append("【今日运势】").append(level).append("\n");
        String[] bads = bad.split("\\|");
        String[] goods = good.split("\\|");
        switch (level){
            case "大凶":
                builder.append("【宜】诸事不宜\n");
                builder.append(bads[0]).append(bads[1]);
                break;
            case "大吉":
                builder.append(goods[0]).append(goods[1]);
                builder.append("【忌】诸事皆宜\n");
                break;
            default:
                builder.append(bads[0]).append(bads[1]);
                builder.append(goods[0]).append(goods[1]);
                break;
        }
        return builder.toString();
    }

    public Divirecord divination(MessageVo vo){
        String Level = "";
        Random random = new Random();
        int getRandom = random.nextInt(10);
        int bad = 0;
        int good = 0;
        Divirecord divirecord = new Divirecord();
        divirecord.setDiviTime(new Timestamp(System.currentTimeMillis()));
        switch (getRandom){
            case 0:
                Level = "大凶";
                good = 2;
                break;
            case 1:
            case 2:
                Level = "小凶";
                break;
            case 3:
            case 4:
                Level = "中平";
                break;
            case 5:
            case 6:
            case 7:
                Level = "小吉";
                break;
            case 8:
            case 9:
                Level = "大吉";
                bad = 2;
        }
        divirecord.setLastLevel(Level);
        List<Divination> div = new ArrayList<>();

        //抽签
        while(bad + good < 4){
            Divination pick = divinationDao.getRandDivination();
            if(pick.getType().equals("good") && bad < 2){
                continue;
            }
            if(pick.getType().equals("bad") && bad == 2){
                continue;
            }
            if(pick.getOnlyUser() != null){
                if(!pick.getOnlyUser().equals(vo.getUser_id())){
                    continue;
                }
            }
            if(pick.getOnlyGroup() != null){
                if(!pick.getOnlyGroup().equals(vo.getGroup_id())){
                    continue;
                }
            }
            boolean notRepeat = true;
            for(Divination each : div){
                if(each.equals(pick)){
                    notRepeat = false;
                    break;
                }
                if(each.getId().equals(pick.getConflict())){
                    notRepeat = false;
                    break;
                }
            }
            if(!notRepeat){
                continue;
            }
            div.add(pick);
            if(pick.getType().equals("good")){
                good++;
            }
            else{
                bad++;
            }
        }
        StringBuilder builder = new StringBuilder();

        //添加运势
        builder.append("【今日运势】").append(Level).append("\n");
        if(Level.equals("大吉")){
            String good1 = "【宜】"+ div.get(0).getTitle()+": "+div.get(0).getContent() + "\n";
            String good2 = "【宜】"+ div.get(1).getTitle()+": "+div.get(1).getContent() + "\n";
            builder.append(good1).append(good2);
            divirecord.setLastGood(good1 + "|" + good2);
            builder.append("【忌】诸事皆宜\n");
            divirecord.setLastBad("");
        }
        else if(Level.equals("大凶")){
            builder.append("【宜】诸事不宜\n");
            divirecord.setLastGood("");
            String bad1 = "【忌】"+ div.get(0).getTitle()+": "+div.get(0).getContent() + "\n";
            String bad2 = "【忌】"+ div.get(1).getTitle()+": "+div.get(1).getContent() + "\n";
            builder.append(bad1).append(bad2);
            divirecord.setLastBad(bad1 + "|" + bad2);
        }
        else{
            String good1 = "【宜】"+ div.get(2).getTitle()+": "+div.get(2).getContent() + "\n";
            String good2 = "【宜】"+ div.get(3).getTitle()+": "+div.get(3).getContent() + "\n";
            divirecord.setLastGood(good1 + "|" + good2);
            String bad1 = "【忌】"+ div.get(0).getTitle()+": "+div.get(0).getContent() + "\n";
            String bad2 = "【忌】"+ div.get(1).getTitle()+": "+div.get(1).getContent() + "\n";
            divirecord.setLastBad(bad1 + "|" + bad2);
            builder.append(good1).append(good2).append(bad1).append(bad2);
        }

        //添加打卡位次
        Divigrouprecord divigrouprecord = divigrouprecordService.getById(vo.getGroup_id());
        if(divigrouprecord == null){
            divigrouprecord = new Divigrouprecord();
            divigrouprecord.setId(vo.getGroup_id());
            divigrouprecord.setLastTime(LocalDate.now());
            divigrouprecord.setNum(1);
            divigrouprecordService.save(divigrouprecord);
        }
        else{
            if(divigrouprecord.getLastTime().equals(LocalDate.now())){
                divigrouprecord.setNum(divigrouprecord.getNum()+1);
                divigrouprecordService.updateById(divigrouprecord);
            }
            else{
                divigrouprecord.setLastTime(LocalDate.now());
                divigrouprecord.setNum(1);
                divigrouprecordService.updateById(divigrouprecord);
            }
        }
        String ranking = "您是本群今日第【" + divigrouprecord.getNum() + "】位求签者";
        divirecord.setDiviRanking(ranking);
        divirecord.setRankingNum(divigrouprecord.getNum());
        divirecord.setLastText(builder.toString());
        return divirecord;
    }

    public String beginDivination(MessageVo vo){
        StringBuilder builder = new StringBuilder();
        StringBuilder achieveBuilder = new StringBuilder();
        Long user_id = vo.getUser_id();
        LocalDate today = LocalDate.now();
        Divirecord divirecord = divirecordService.getById(user_id);

        if(divirecord == null){
            builder.append("您是第一次打卡！\n");
            achieveBuilder.append(achievementService.wonAchieve(9L, vo.getUser_id(), vo.getGroup_id()));
            achieveBuilder.append(achievementService.checkTime(vo));
            builder.append("\n您的签文：\n");
            Divirecord record = this.divination(vo);
            record.setId(user_id);
            record.setLastTime(today);
            record.setContinuity(1);
            record.setCumulate(1);
            if(record.getLastLevel().equals("大吉")){
                record.setBigGoodDays(1);
            }
            else{
                record.setBigGoodDays(0);
            }
            if(record.getLastLevel().equals("大凶")){
                record.setBigBadDays(1);
            }
            else{
                record.setBigBadDays(0);
            }
            record.setDiviGroup(vo.getGroup_id());
            divirecord = record;
            builder
                    .append(record.getLastText())
                    .append(record.getDiviRanking())
                    .append("\n");
            if(divirecord.getRankingNum() == 1){
                achieveBuilder.append(achievementService.wonAchieve(12L, vo.getUser_id(), vo.getGroup_id()));
            }
            divirecordService.save(record);
        }
        else{
            if(divirecord.getLastTime().equals(today)){
                if(!divirecord.getDiviGroup().equals(vo.getGroup_id())){
                    builder.append("您已在其他群求过签！\n");
                }
                else{
                    builder.append("今天您已求过签!\n");
                }
                builder.append("\n您的签文：\n");
                builder
                        .append(divirecord.getLastText());
            }
            else{
                if(divirecord.getLastTime().plus(1, ChronoUnit.DAYS).equals(today)){
                    divirecord.setCumulate(divirecord.getCumulate()+1);
                    divirecord.setContinuity(divirecord.getContinuity()+1);
                }
                else{
                    builder.append("昨天您没有求签~\n\n");
                    achieveBuilder.append(achievementService.wonAchieve(13L, vo.getUser_id(), vo.getGroup_id()));
                    divirecord.setCumulate(divirecord.getCumulate()+1);
                    divirecord.setContinuity(1);
                }
                achieveBuilder.append(achievementService.checkTime(vo));
                builder.append("您的签文：\n");
                Divirecord record = this.divination(vo);

                if(divirecord.getBigGoodDays() == null){
                    divirecord.setBigGoodDays(0);
                }
                if(divirecord.getBigBadDays() == null){
                    divirecord.setBigBadDays(0);
                }

                record.setBigGoodDays(record.getLastLevel().equals("大吉") ? divirecord.getBigGoodDays()+1 : 0);
                record.setBigBadDays(record.getLastLevel().equals("大凶") ? divirecord.getBigBadDays()+1 : 0);

                if(record.getBigGoodDays() >= 3){
                    achieveBuilder.append(achievementService.wonAchieve("欧皇", vo.getUser_id(), vo.getGroup_id()));
                }
                if(record.getBigBadDays() >= 3){
                    achieveBuilder.append(achievementService.wonAchieve("非酋", vo.getUser_id(), vo.getGroup_id()));
                }

                record.setCumulate(divirecord.getCumulate());
                record.setContinuity(divirecord.getContinuity());
                record.setLastTime(today);
                record.setId(divirecord.getId());
                record.setDiviGroup(vo.getGroup_id());
                divirecord = record;
                builder
                        .append(divirecord.getLastText())
                        .append(divirecord.getDiviRanking())
                        .append("\n");
            }

            divirecordService.updateById(divirecord);
            if(divirecord.getRankingNum() == 1){
                achieveBuilder.append(achievementService.wonAchieve(12L, vo.getUser_id(), vo.getGroup_id()));
            }
        }
        if(divirecord.getContinuity() >= 7){
            achieveBuilder.append(achievementService.wonAchieve(11L, vo.getUser_id(), vo.getGroup_id()));
        }
        builder.append("\n您的打卡记录：\n连续求签【").append(divirecord.getContinuity()).append("】天\n");
        builder.append("累计求签【").append(divirecord.getCumulate()).append("】天\n");
        if(!achieveBuilder.toString().equals("")){
            builder
                    .append("获得成就：\n")
                    .append(achieveBuilder);
        }
        return builder.toString();
    }
}
