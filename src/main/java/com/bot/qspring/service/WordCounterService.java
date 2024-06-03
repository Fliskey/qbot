package com.bot.qspring.service;

import com.bot.qspring.entity.po.Wordcounter;
import com.bot.qspring.entity.vo.MessageVo;
import com.bot.qspring.mapper.WordcounterMapper;
import com.bot.qspring.service.dbauto.WordcounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class WordCounterService {

    @Autowired
    private WordcounterService wordcounterService;

    @Autowired
    private WordcounterMapper wordcounterMapper;

    @Autowired
    private SenderService senderService;

    public String getFreeTime(LocalDateTime until) {
        Duration duration = Duration.between(LocalDateTime.now(), until);
        return duration.toMinutesPart() + "分" + duration.toSecondsPart() + "秒";
    }

    public String checkWordCounter(MessageVo vo) {
        //禁言临界条数
        int edge = 7;

        String msg = vo.getMessage();
        Long userId = vo.getUser_id();
        LocalDate today = LocalDate.now();
        Wordcounter wordcounter = wordcounterMapper.selectById(userId);
        if (wordcounter == null) {
            wordcounter = new Wordcounter();
            wordcounter.setId(userId);
            wordcounterService.save(wordcounter);
        }
        LocalDate getDate = wordcounter.getDate();
        if (getDate != null && getDate.equals(today)) {
            LocalDateTime stopUntil = wordcounter.getStopUntil();
            if (stopUntil != null && stopUntil.isAfter(LocalDateTime.now())) {
                //正在封禁中
                return "";
            } else {
                //未封禁或过封禁，今天
                if (vo.getMessage().equals(wordcounter.getWord())) {
                    Integer count = wordcounter.getCounter();
                    if (count == null) {
                        wordcounter.setCounter(1);
                    } else {
                        wordcounter.setCounter(wordcounter.getCounter() + 1);
                    }
                    wordcounterService.updateById(wordcounter);
                    if (wordcounter.getCounter() == edge - 4) {
                        return "不可以调戏本bot！";
                    } else if (wordcounter.getCounter() >= edge) {
                        //封禁
                        wordcounter.setStopUntil(LocalDateTime.now().plusMinutes(5L));
                        wordcounterService.updateById(wordcounter);
                        StringBuilder builder = new StringBuilder();
                        String nextTime = wordcounter.getStopUntil().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                        builder
                                .append("不理你了，哼唧！\n")
                                .append(nextTime)
                                .append("以后再来找我！！");
                        senderService.sendGroup(vo.getUser_id(), vo.getGroup_id(), builder.toString());
                        return builder.toString();
                    } else if (wordcounter.getCounter() > edge - 4) {
                        StringBuilder builder = new StringBuilder();
                        Random random = new Random();
                        int getRand = random.nextInt(7);
                        switch (getRand) {
                            case 0:
                                builder.append("李四复读机吗？\n");
                                builder.append("李在赣神魔？");
                                break;
                            case 1:
                                builder.append("此时一位测试工程师走进酒吧点了一杯烫烫烫的锟斤拷\n");
                                break;
                            case 2:
                                builder.append("你这是在做图灵测试吗一直戳我？？\n我不理你了哦");
                                break;
                            case 3:
                                builder.append("建国后不许复读机成精！");
                                break;
                            case 4:
                                builder.append("“智能bot已停止为您服务~~~”");
                                break;
                            case 5:
                                builder.append("How old are you?");
                                break;
                            case 6:
                                builder.append("喵喵喵喵喵喵喵喵喵喵喵喵");
                                break;
                        }
                        return builder.toString();
                    } else {
                        return "YES";
                    }
                }
            }
        }
        wordcounter.setWord(vo.getMessage());
        wordcounter.setCounter(1);
        wordcounter.setDate(today);
        wordcounterService.updateById(wordcounter);
        return "YES";
    }
}
