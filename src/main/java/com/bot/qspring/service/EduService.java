package com.bot.qspring.service;

import cn.hutool.core.util.RandomUtil;
import com.bot.qspring.dao.EduDao;
import com.bot.qspring.entity.*;
import com.bot.qspring.model.Vo.MessageVo;
import com.bot.qspring.service.dbauto.EduRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class EduService {
    @Autowired
    EduRecordService eduRecordService;

    @Autowired
    EduDao eduDao;

    @Autowired
    SenderService senderService;

    Boolean freeNow = false;

    public String queryNow() {
        EduRecord lastRecord = eduDao.getLastRecord();
        StringBuilder builder = new StringBuilder();
        if (lastRecord == null || lastRecord.getEndTime() != null && lastRecord.getEndTime().isBefore(LocalDateTime.now())) {
            builder.append("bot正在摸鱼喵(・ω・)");
        } else {
            if ("睡觉".equals(lastRecord.getType())) {
                builder.append("bot正在睡觉喵(¦3[▓▓]");
            } else {
                builder.append("bot正在").append(lastRecord.getType());
                switch (lastRecord.getType()) {
                    case "看":
                    case "读":
                        builder.append("《").append(lastRecord.getName()).append("》");
                        break;
                    case "练习":
                    case "吃":
                        builder.append(lastRecord.getName());
                        break;
                    default:
                        break;
                }
                builder.append("喵");
            }
            Duration duration = Duration.between(LocalDateTime.now(), lastRecord.getEndTime());
            builder.append("(").append(duration.toMinutes()).append(")");
        }
        return builder.toString();
    }

    public String updateLast() {
        String type;
        EduRecord lastRecord = eduDao.getLastRecord();
        if (lastRecord == null) {
            lastRecord = new EduRecord();
            lastRecord.setType("摸鱼");
            lastRecord.setOp(0L);
            lastRecord.setStartTime(null);
            lastRecord.setEndTime(LocalDateTime.now());
            eduRecordService.save(lastRecord);
            type = "bot初始化";
        } else if (lastRecord.getEndTime() == null) {
            lastRecord.setEndTime(LocalDateTime.now());
            eduRecordService.updateById(lastRecord);
            type = "结束不定时任务";
        } else if (lastRecord.getEndTime().isBefore(LocalDateTime.now())) {
            LocalDateTime lastEnd = lastRecord.getEndTime();
            lastRecord = new EduRecord();
            lastRecord.setOp(0L);
            lastRecord.setStartTime(lastEnd);
            lastRecord.setEndTime(LocalDateTime.now());
            lastRecord.setType("摸鱼");
            eduRecordService.save(lastRecord);
            type = "补足上次活动到现在的空余时间";
        } else {
            lastRecord.setEndTime(LocalDateTime.now());
            eduRecordService.updateById(lastRecord);
            type = "打断上次时间";
        }
        freeNow = true;
        return type;
    }

    public Integer getMinute(String type) {
        switch (type) {
            case "吃":
                return RandomUtil.randomInt(5, 30);
            case "练习":
                return RandomUtil.randomInt(30, 60);
            case "看":
                return RandomUtil.randomInt(90, 240);
            case "读":
                return RandomUtil.randomInt(10, 30);
            case "睡觉":
                return RandomUtil.randomInt(60, 240);
            default:
                return 60;
        }
    }

    public String goToMeal(MessageVo msg) {
        StringBuilder builder = new StringBuilder();
        EduRecord record = new EduRecord();
        record.setType("吃");
        EduDish eduDish = eduDao.getRandDish();
        record.setEduId(eduDish.getId());
        record.setName(eduDish.getName());
        if (msg == null) {
            record.setOp(0L);
        } else {
            record.setOp(msg.getUser_id());
        }
        record.setStartTime(LocalDateTime.now());
        record.setEndTime(LocalDateTime.now().plusMinutes(getMinute("吃")));
        eduRecordService.save(record);
        freeNow = false;
        builder.append(queryNow());
        return builder.toString();
    }

    public String goToReadBook(MessageVo msg) {
        StringBuilder builder = new StringBuilder();
        EduRecord record = new EduRecord();
        record.setType("读");
        EduBook eduBook = eduDao.getRandBook();
        record.setEduId(eduBook.getId());
        record.setName(eduBook.getName());
        if (msg == null) {
            record.setOp(0L);
        } else {
            record.setOp(msg.getUser_id());
        }
        record.setStartTime(LocalDateTime.now());
        record.setEndTime(LocalDateTime.now().plusMinutes(getMinute("读")));
        eduRecordService.save(record);
        freeNow = false;
        builder.append(queryNow());
        return builder.toString();
    }

    public String goToWatchMovie(MessageVo msg) {
        StringBuilder builder = new StringBuilder();
        EduRecord record = new EduRecord();
        record.setType("看");
        EduMovie eduMovie = eduDao.getRandMovie();
        record.setEduId(eduMovie.getId());
        if (msg == null) {
            record.setOp(0L);
        } else {
            record.setOp(msg.getUser_id());
        }
        record.setName(eduMovie.getName());
        record.setStartTime(LocalDateTime.now());
        record.setEndTime(LocalDateTime.now().plusMinutes(getMinute("看")));
        eduRecordService.save(record);
        freeNow = false;
        builder.append(queryNow());
        return builder.toString();
    }

    public String goToSport(MessageVo msg) {
        StringBuilder builder = new StringBuilder();
        EduRecord record = new EduRecord();
        record.setType("练习");
        EduSport eduSport = eduDao.getRandSport();
        record.setEduId(eduSport.getId());
        if (msg == null) {
            record.setOp(0L);
        } else {
            record.setOp(msg.getUser_id());
        }
        record.setStartTime(LocalDateTime.now());
        record.setEndTime(LocalDateTime.now().plusMinutes(getMinute("练习")));
        record.setName(eduSport.getName());
        eduRecordService.save(record);
        freeNow = false;
        builder.append(queryNow());
        return builder.toString();
    }

    public String goToSleep(MessageVo msg, Boolean isNightAuto) {
        StringBuilder builder = new StringBuilder();
        EduRecord record = new EduRecord();
        record.setType("睡觉");
        if (msg == null) {
            record.setOp(0L);
        } else {
            record.setOp(msg.getUser_id());
        }
        record.setStartTime(LocalDateTime.now());
        int plus = getMinute("睡觉");
        if (isNightAuto) {
            plus = RandomUtil.randomInt(420, 450);
        }
        record.setEndTime(LocalDateTime.now().plusMinutes(plus));
        eduRecordService.save(record);
        freeNow = false;
        builder.append(queryNow());
        return builder.toString();
    }

    private String checkMealTime() {
        if (LocalDateTime.now().getHour() == 6 && LocalDateTime.now().plusMinutes(30).getHour() == 7) {
            return "快吃早饭了";
        }
        if (LocalDateTime.now().getHour() == 11 && LocalDateTime.now().plusMinutes(30).getHour() == 12) {
            return "快吃午饭了";
        }
        if (LocalDateTime.now().getHour() == 17 && LocalDateTime.now().plusMinutes(30).getHour() == 18) {
            return "快吃晚饭了";
        }
        return "";
    }

    private Boolean checkFree() {
        EduRecord lastRecord = eduDao.getLastRecord();
        freeNow = lastRecord == null || lastRecord.getEndTime().isBefore(LocalDateTime.now()) || "摸鱼".equals(lastRecord.getType());
        return freeNow;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void autoCheckFreeEveryMinute() {
        if (!freeNow && checkFree()) {
            EduRecord lastRecord = eduDao.getLastRecord();
            StringBuilder builder = new StringBuilder();
            switch (lastRecord.getType()) {
                case "看":
                    builder.append("看电影结束~");
                    break;
                case "读":
                    builder.append("阅读结束~");
                    break;
                case "睡觉":
                    if (LocalDateTime.now().getHour() < 12) {
                        builder.append("早安！bot睡醒啦！");
                    } else if (LocalDateTime.now().getHour() < 18) {
                        builder.append("午安！bot睡醒啦！");
                    } else {
                        builder.append("晚上好！bot睡醒啦！");
                    }
                    break;
                case "练习":
                    builder.append("运动结束~");
                    break;
                case "吃":
                    builder.append("bot吃完啦！");
                    break;
            }
            senderService.sendGroup(740093557L, builder.toString());
        }
    }

    @Scheduled(cron = "0 0 7 * * ?")
    private void autoGoForBreakfast() {
        updateLast();
        goToMeal(null);
        EduRecord last = eduDao.getLastRecord();
        senderService.sendGroup(740093557L, "早上好！今天bot的早餐是" + last.getName() + "！ovo");
    }

    @Scheduled(cron = "0 0 12 * * ?")
    private void autoGoForLunch() {
        updateLast();
        goToMeal(null);
        EduRecord last = eduDao.getLastRecord();
        senderService.sendGroup(740093557L, "中午好！今天bot的午餐是" + last.getName() + "！qwq");
    }

    @Scheduled(cron = "0 0 18 * * ?")
    private void autoGoForDinner() {
        updateLast();
        goToMeal(null);
        EduRecord last = eduDao.getLastRecord();
        senderService.sendGroup(740093557L, "晚上好！今天bot的晚餐是" + last.getName() + "！=v=");
    }

    @Scheduled(cron = "0 0/10 8-11,14-17,20-22 * * ?")
    private void autoGoPlaying() {
        StringBuilder builder = new StringBuilder();
        if (!checkFree()) {
            return;
        }
        updateLast();
        int rand = RandomUtil.randomInt(0, 20);
        switch (rand) {
            case 0:
            case 1:
            case 2:
            case 3:
                // 看书
                goToReadBook(null);
                builder.append("阅读时间📕");
                break;

            case 4:
            case 5:
            case 6:
            case 7:
                // 零食
                goToMeal(null);
                builder.append("零食时间🍰");
                break;

            case 8:
            case 9:
            case 10:
            case 11:
                // 体育运动
                goToSport(null);
                builder.append("运动时间🏃‍");
                break;

            case 12:
                // 打盹
                goToSleep(null, false);
                builder.append("打盹时间🛏");
                break;

            case 13:
                // 看电影
                goToWatchMovie(null);
                builder.append("电影时间📽");
                break;

            default:
                // 摸鱼
                builder.append("摸鱼时间🐟");
                break;
        }
        senderService.sendGroup(740093557L, builder.toString());
    }

    @Scheduled(cron = "0 0 23 * * ?")
    private void autoGoSleeping() {
        updateLast();
        goToSleep(null, true);
        senderService.sendGroup(740093557L, "bot睡觉了，晚安~");
    }

    private String notFreeDo() {
        StringBuilder builder = new StringBuilder();
        builder.append(queryNow());
        EduRecord last = eduDao.getLastRecord();
        Duration duration = Duration.between(LocalDateTime.now(), last.getEndTime());
        builder.append("，请").append(duration.toMinutes()).append("分钟后再安排我做别的吧");
        return builder.toString();
    }

    public String callToMeal(MessageVo msg) {
        if (!checkFree()) {
            return notFreeDo();
        }
        updateLast();
        return goToMeal(msg);
    }

    public String callToSport(MessageVo msg) {
        if (!checkFree()) {
            return notFreeDo();
        }
        updateLast();
        return goToSport(msg);
    }

    public String callToReadBook(MessageVo msg) {
        if (!checkFree()) {
            return notFreeDo();
        }
        updateLast();
        return goToReadBook(msg);
    }

    public String callToWatchMovie(MessageVo msg) {
        if (!checkFree()) {
            return notFreeDo();
        }
        updateLast();
        return goToWatchMovie(msg);
    }

    public String callToSleep(MessageVo msg) {
        if (!checkFree()) {
            return notFreeDo();
        }
        updateLast();
        return goToSleep(msg, false);
    }

    public String callToBreak(MessageVo msg) {
        updateLast();
        return "好的喵";
    }

    public List<EduRecord> getDayRecord(LocalDate day) {
        return eduDao.getRecodeListByDay(day, day.plusDays(1));
    }

    private Long timeRangeOfEduRecord(EduRecord eduRecord) {
        Duration duration = Duration.between(eduRecord.getStartTime(), eduRecord.getEndTime());
        return duration.toMinutes();
    }

    private StringBuilder handleDayEduRecordList(List<EduRecord> eduRecordList) {
        StringBuilder builder = new StringBuilder();
        Map<String, Long> lifeCounterMap = new HashMap<>();

        if (!eduRecordList.get(0).getStartTime().equals(LocalDateTime.of(eduRecordList.get(0).getStartTime().toLocalDate(), LocalTime.MIN))) {
            EduRecord record = new EduRecord();
            record.setType("睡觉");
            record.setStartTime(LocalDateTime.of(eduRecordList.get(0).getStartTime().toLocalDate(), LocalTime.MIN));
            record.setEndTime(eduRecordList.get(0).getStartTime());
            eduRecordList.add(0, record);
        }
        EduRecord lastRecord = eduRecordList.get(eduRecordList.size() - 1);
        if (lastRecord.getStartTime().getDayOfMonth() != lastRecord.getEndTime().getDayOfMonth()) {
            lastRecord.setEndTime(LocalDateTime.of(lastRecord.getStartTime().toLocalDate(), LocalTime.MAX));
            eduRecordList.set(eduRecordList.size() - 1, lastRecord);
        }
        boolean isTodayLast = false;

        for (EduRecord eduRecord : eduRecordList) {
            // Save actions and how long it takes.
            Long timeRange = timeRangeOfEduRecord(eduRecord);
            if (eduRecord.getEndTime().isAfter(LocalDateTime.now())) {
                isTodayLast = true;
                EduRecord tempEduRecord = new EduRecord();
                tempEduRecord.setStartTime(eduRecord.getStartTime());
                tempEduRecord.setEndTime(LocalDateTime.now());
                timeRange = timeRangeOfEduRecord(tempEduRecord);
            }
            if (lifeCounterMap.containsKey(eduRecord.getType())) {
                Long sum = timeRange + lifeCounterMap.get(eduRecord.getType());
                lifeCounterMap.put(eduRecord.getType(), sum);
            } else {
                lifeCounterMap.put(eduRecord.getType(), timeRange);
            }

            if ("摸鱼".equals(eduRecord.getType())) {
                continue;
            }
            builder.append("\n");
            builder.append(String.format("%02d:%02d", eduRecord.getStartTime().getHour(), eduRecord.getStartTime().getMinute()));
            if (isTodayLast) {
                builder.append(String.format("-%02d:%02d:", LocalTime.now().getHour(), LocalTime.now().getMinute()));
            } else {
                builder.append(String.format("-%02d:%02d:", eduRecord.getEndTime().getHour(), eduRecord.getEndTime().getMinute()));
            }

            switch (eduRecord.getType()) {
                case "吃":
                    if (eduRecord.getStartTime().getHour() < 10) {
                        builder.append("🍽早餐 ").append(eduRecord.getName());
                    } else if (eduRecord.getStartTime().getHour() < 12) {
                        builder.append("🍽早中餐 ").append(eduRecord.getName());
                    } else if (eduRecord.getStartTime().getHour() < 13) {
                        builder.append("🍽午餐 ").append(eduRecord.getName());
                    } else if (eduRecord.getStartTime().getHour() < 18) {
                        builder.append("🍽下午茶 ").append(eduRecord.getName());
                    } else if (eduRecord.getStartTime().getHour() < 19) {
                        builder.append("🍽晚餐 ").append(eduRecord.getName());
                    } else {
                        builder.append("🍽夜宵 ").append(eduRecord.getName());
                    }
                    break;
                case "看":
                    builder.append("📽看电影 ").append(eduRecord.getName());
                    break;
                case "读":
                    builder.append("📕阅读 ").append(eduRecord.getName());
                    break;
                case "睡觉":
                    builder.append("🛏睡觉 ");
                    break;
                case "练习":
                    builder.append("🏃运动‍ ").append(eduRecord.getName());
                    break;
            }
            if (isTodayLast) {
                builder.append("(").append(Duration.between(LocalDateTime.now(), eduRecord.getEndTime()).toMinutes()).append(")");
            }
        }
        Long lifeCountMaxTimeSum = Collections.max(lifeCounterMap.values());
        Long lengthOfLongestSquare = 8L;
        List<Map.Entry<String, Long>> lifeCounterEntryList = new ArrayList<>(lifeCounterMap.entrySet());
        lifeCounterEntryList.sort(Map.Entry.comparingByValue());
        Collections.reverse(lifeCounterEntryList);
        lifeCounterEntryList.forEach(entry -> {
            switch (entry.getKey()) {
                case "摸鱼":
                    builder.append("\n摸鱼：");
                    break;
                case "吃":
                    builder.append("\n吃饭：");
                    break;
                case "看":
                    builder.append("\n观影：");
                    break;
                case "读":
                    builder.append("\n阅读：");
                    break;
                case "睡觉":
                    builder.append("\n睡觉：");
                    break;
                case "练习":
                    builder.append("\n运动：");
                    break;
            }
            for (int i = 0; i < Math.ceil(lengthOfLongestSquare.doubleValue() * entry.getValue().doubleValue() / lifeCountMaxTimeSum.doubleValue()); i++) {
                builder.append("█");
            }
            builder.append("(").append(entry.getValue()).append(")");
        });
        return builder;
    }

    public String yesterdayRecords() {
        StringBuilder builder = new StringBuilder();
        List<EduRecord> eduRecordList = getDayRecord(LocalDate.now().minusDays(1));
        if (eduRecordList == null || eduRecordList.isEmpty()) {
            return "昨天bot摸了一天鱼";
        }
        builder.append("bot昨日日志：");
        builder.append(handleDayEduRecordList(eduRecordList));
        return builder.toString();
    }

    public String todayRecords() {
        StringBuilder builder = new StringBuilder();
        List<EduRecord> eduRecordList = getDayRecord(LocalDate.now());
        if (eduRecordList == null || eduRecordList.isEmpty()) {
            return "今天bot摸了一天鱼";
        }
        builder.append("bot今日日志：");
        builder.append(handleDayEduRecordList(eduRecordList));
        return builder.toString();
    }

    private StringBuilder handleDishes(List<EduRecord> eduRecordList) {
        StringBuilder builder = new StringBuilder();
        for (EduRecord eduRecord : eduRecordList) {
            if ("吃".equals(eduRecord.getType())) {
                builder.append("\n");
                if (eduRecord.getStartTime().getHour() < 10) {
                    builder.append("早餐 ").append(eduRecord.getName());
                } else if (eduRecord.getStartTime().getHour() < 12) {
                    builder.append("早中餐 ").append(eduRecord.getName());
                } else if (eduRecord.getStartTime().getHour() < 13) {
                    builder.append("午餐 ").append(eduRecord.getName());
                } else if (eduRecord.getStartTime().getHour() < 18) {
                    builder.append("下午茶 ").append(eduRecord.getName());
                } else if (eduRecord.getStartTime().getHour() < 19) {
                    builder.append("晚餐 ").append(eduRecord.getName());
                } else {
                    builder.append("夜宵 ").append(eduRecord.getName());
                }
            }
        }
        return builder;
    }

    public String yesterdayDishes() {
        StringBuilder builder = new StringBuilder();
        List<EduRecord> eduRecordList = getDayRecord(LocalDate.now());
        builder.append("bot昨天吃：");
        builder.append(handleDishes(eduRecordList));
        if ("bot昨天吃：".equals(builder.toString())) {
            return "bot昨天饿肚肚";
        }
        return builder.toString();
    }

    public String todayDishes() {
        StringBuilder builder = new StringBuilder();
        List<EduRecord> eduRecordList = getDayRecord(LocalDate.now());
        builder.append("bot今天吃：");
        builder.append(handleDishes(eduRecordList));
        if ("bot今天吃：".equals(builder.toString())) {
            return "bot今天饿肚肚";
        }
        return builder.toString();
    }

}
