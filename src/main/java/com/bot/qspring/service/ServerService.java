package com.bot.qspring.service;

import com.bot.qspring.dao.NoticeDao;
import com.bot.qspring.entity.po.GroupNotice;
import com.bot.qspring.entity.po.ServiceSwitcher;
import com.bot.qspring.entity.vo.MessageVo;
import com.bot.qspring.service.dbauto.ServiceSwitcherService;
import com.bot.qspring.service.stopped.AppPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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

    @Autowired
    private AdminService adminService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private AppDiceService appDiceService;

    @Autowired
    private AppSwitcherService appSwitcherService;

    @Autowired
    private ServiceSwitcherService serviceSwitcherService;

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private EduService eduService;

    public void sendVerifyCode(String qq, String code) {
        senderService.sendPrivate(Long.valueOf(qq), "您的验证码是：" + code + "，验证码5分钟内有效。");
    }

    private String getGuiding(MessageVo vo) {
        StringBuilder builder = new StringBuilder();
        String[] msgList = vo.getMessage().split(" ");
        //目前命令较少，未来可做分页
        if (msgList.length == 2 || msgList[2].equals("1")) {
            builder
                    .append("可使用指令集：\n")
                    .append("【bot help】显示此段指令集\n")
                    .append("【bot github】显示bot在Github的地址\n")
                    .append("【求签】求取一条签文\n")
                    .append("【求签统计】今日群运势统计\n")
                    .append("【求签排名】今日求签前五并at\n")
                    .append("【重置计数】让不理你的bot重新理你\n")
                    .append("【xxx什么梗】查看某梗来源（仅支持冷门梗）\n")
                    .append("【成就】查看本人已获成就\n")
                    .append("【成就图鉴】查看本群成就图鉴\n")
                    .append("【全服成就图鉴】查看所有可获得的成就\n")
                    .append("【未得成就】查看本人未获得的成就\n")
                    .append("【报名 活动名】报名一项活动，活动不存在则新建\n")
                    .append("【取消报名 活动名】取消活动报名\n")
                    .append("【报名统计 活动名】显示该活动所有报名者\n")
                    .append("【报名通知 活动名】一键at报名该活动的成员\n");
        }
        return builder.toString();
    }

    private String getPrivateGuiding() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("私聊可使用指令集：\n")
                .append("【成就】查看本人已获成就\n")
                .append("【今日签文】查看今日签文\n")
                .append("【github】显示bot的Github源码，包含即触发\n")
                .append("【help】显示此段指令集，包含即触发\n")
                .append("更多功能请前往群内使用~");
        return builder.toString();
    }

    private String getPokeRet() {
        String ret = "";
        Random random = new Random();
        int getInt = random.nextInt(5);
        switch (getInt) {
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


    public void handleAll(MessageVo messageVo) {
        switch (messageVo.getPost_type()) {
            case "meta_event":
                break;
            case "notice":
                switch (messageVo.getSub_type()) {
                    case "poke":
                        //拍一拍
                        if (messageVo.getSelf_id().equals(messageVo.getTarget_id())) {
                            String ret = achievementService.wonAchieve(7L, messageVo.getUser_id(), messageVo.getGroup_id());
                            if (!ret.equals("")) {
                                ret = "获得成就：\n" + ret;
                                senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), ret);
                            } else {
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
                switch (messageVo.getMessage_type()) {
                    case "private":
//                        handlePrivate(messageVo);
                        break;
                    case "group":
                        handleGroup(messageVo);
                        break;
                }
                break;
        }
    }

    public void handlePrivate(MessageVo messageVo) {
        String msg = messageVo.getMessage();
        System.out.println(msg);
        StringBuilder builder = new StringBuilder();
        if (msg.contains("【宜】") || msg.contains("【忌】")) {
            builder.append(achievementService.wonAchieve(2L, messageVo.getUser_id(), messageVo.getGroup_id()));
            if (!builder.toString().equals("")) {
                builder = new StringBuilder()
                        .append("获得成就：\n")
                        .append(builder);
                senderService.sendPrivate(messageVo.getUser_id(), builder.toString());
            }
            builder.append("发起者：").append(messageVo.getSender()).append("\n");
            builder.append("签文：").append(msg);
            senderService.sendPrivate(2214106974L, builder.toString());
            return;
        } else if (msg.toLowerCase().contains("help")) {
            builder.append(getPrivateGuiding());
        } else if (msg.toLowerCase().contains("github")) {
            builder.append("https://github.com/Fliskey/qbot");
        } else if (msg.startsWith("admin")) {
            builder.append(adminService.handleAdminAll(messageVo));
        }
        if (!builder.toString().equals("")) {
            if (!msg.startsWith("admin") && msg.length() < 20) {
//                wordCounterService.checkWordCounter(messageVo);
            }
            senderService.sendPrivate(messageVo.getUser_id(), builder.toString());
            return;
        }

        if (msg.equals("成就")) {
            builder.append(achievementService.getOnesAchieve(messageVo.getUser_id()));
        } else if (msg.equals("今日签文")) {
            builder.append(appDivinationService.getDiviRecord(messageVo));
        }
        if (builder.toString().equals("")) {
            builder.append("很抱歉，这个命令暂不支持，bot已将您的需求发送给开发者。您可以使用以下命令：\n");
            senderService.sendPrivate(2214106974L, messageVo.toString());
            builder.append(this.getPrivateGuiding());
        }
        senderService.sendPrivate(messageVo.getUser_id(), builder.toString());
    }

    private String checkCounter(MessageVo messageVo) {
        String ret = wordCounterService.checkWordCounter(messageVo);
        StringBuilder builder = new StringBuilder();
        builder.append(ret);
        if (!ret.equals("YES")) {
            System.out.println(ret);

            String achieve = achievementService.wonAchieve(1L, messageVo.getUser_id(), messageVo.getGroup_id());
            if (!achieve.equals("")) {
                builder
                        .append("\n\n获得成就：\n")
                        .append(achieve);
            }
        }
        return builder.toString();
    }

    public void handleGroup(MessageVo messageVo) {
        String msg = messageVo.getMessage();
        System.out.println(msg);

        Boolean botAction = false;

        ServiceSwitcher serviceSwitcher = serviceSwitcherService.getById(messageVo.getGroup_id());
        if (serviceSwitcher != null && serviceSwitcher.getIdStopped()) {
            String result = appSwitcherService.switchJudge(serviceSwitcher, messageVo);
            if (Objects.equals(result, "Off")) {
                return;
            } else {
                senderService.sendGroup(messageVo.getGroup_id(), result);
            }
        }

        StringBuilder builder = new StringBuilder();
//        String checkCounterResult = checkCounter(messageVo);
//        if(!checkCounterResult.equals("YES")){
//            builder.append(checkCounterResult);
//        }

        //骰子部分
//        if(msg.startsWith(".")){
//            builder.append(appDiceService.diceEnter(messageVo));
//        }

        //部分匹配
        if (msg.contains("【宜】") || msg.contains("【忌】")) {
            builder.append(appDivinationService.selfGoodBad(messageVo));
            if (!builder.toString().equals("")) {
                builder = new StringBuilder()
                        .append("获得成就：\n")
                        .append(builder);
                senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());
            }
            return;
        } else if (msg.toLowerCase().startsWith("bot help")) {
            builder.append(getGuiding(messageVo));
        } else if (msg.contains("bot") && msg.contains("off")) {
            builder.append(appSwitcherService.switchOff(messageVo));
        } else if (msg.toLowerCase().contains("bot")) {
            if (msg.toLowerCase().contains("github")) {
                builder.append("https://github.com/Fliskey/qbot");
            } else {
                botAction = true;
                switch (msg) {
                    case "bot在干啥":
                    case "bot在干嘛":
                    case "bot在做啥":
                    case "bot干啥呢":
                    case "bot干嘛呢":
                    case "bot做啥呢":
                    case "bot在干啥呢":
                    case "bot在干嘛呢":
                    case "bot在做啥呢":
                    case "bot在干什么":
                    case "bot在干什么呢":
                    case "bot在做什么":
                    case "bot在做什么呢":
                        builder.append(eduService.queryNow());
                        break;
                    case "bot去睡觉":
                        builder.append(eduService.callToSleep(messageVo));
                        break;
                    case "bot去吃饭":
                        builder.append(eduService.callToMeal(messageVo));
                        break;
                    case "bot去看书":
                    case "bot去读书":
                    case "bot去阅读":
                        builder.append(eduService.callToReadBook(messageVo));
                        break;
                    case "bot去看电影":
                        builder.append(eduService.callToWatchMovie(messageVo));
                        break;
                    case "bot去锻炼":
                    case "bot去运动":
                        builder.append(eduService.callToSport(messageVo));
                        break;
                    case "bot去休息":
                        builder.append(eduService.callToBreak(messageVo));
                        break;
                    case "bot昨天在干啥":
                    case "bot昨天在干嘛":
                    case "bot昨天在做啥":
                    case "bot昨天干了啥":
                    case "bot昨天干了嘛":
                    case "bot昨天做了啥":
                    case "bot昨天干啥了":
                    case "bot昨天干嘛了":
                    case "bot昨天做啥了":
                    case "bot昨天干了什么":
                    case "bot昨天干了什么呢":
                    case "bot昨天做了什么":
                    case "bot昨天做了什么呢":
                        builder.append(eduService.yesterdayRecords());
                        break;
                    case "bot今天在干啥":
                    case "bot今天在干嘛":
                    case "bot今天在做啥":
                    case "bot今天干了啥":
                    case "bot今天干了嘛":
                    case "bot今天做了啥":
                    case "bot今天干啥了":
                    case "bot今天干嘛了":
                    case "bot今天做啥了":
                    case "bot今天干了什么":
                    case "bot今天干了什么呢":
                    case "bot今天做了什么":
                    case "bot今天做了什么呢":
                        builder.append(eduService.todayRecords());
                        break;
                    case "bot昨天吃了吗":
                    case "bot昨天吃什么":
                    case "bot昨天吃了啥":
                    case "bot昨天吃啥了":
                        builder.append(eduService.yesterdayDishes());
                        break;
                    case "bot今天吃了吗":
                    case "bot今天吃什么":
                    case "bot今天吃了啥":
                    case "bot今天吃啥了":
                        builder.append(eduService.todayDishes());
                        break;
                }
            }
        } else if (!msg.equals("通知") && !msg.equals("通知删除") && msg.startsWith("通知")) {
            if (senderService.isAdmin(messageVo.getGroup_id(), messageVo.getUser_id())) {
                builder.append(noticeService.addNotice(messageVo));
            } else {
                builder.append("加通知权限不足");
            }
        } else if (msg.startsWith("报名统计")) {
            builder.append(appPartyService.staticsParty(messageVo));
        } else if (msg.startsWith("报名通知")) {
            builder.append(appPartyService.noticeParty(messageVo));
        } else if (msg.startsWith("报名")) {
            builder.append(appPartyService.signUpParty(messageVo));
        } else if (msg.startsWith("取消报名")) {
            builder.append(appPartyService.cancelSignUpParty(messageVo));
        } else if (msg.contains("什么梗")) {
            builder.append(appDivinationService.getMemeFrom(messageVo));
        }


        //全词匹配前确认Builder是否有需要发送的内容
        if (!builder.toString().equals("")) {
            senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());
            return;
        }
        //全词匹配
        switch (msg) {
            case "通知": {
                GroupNotice notice = noticeDao.getNoticeById(messageVo.getGroup_id(), LocalDate.now());
                if (notice != null) {
                    String card = senderService.getCard(messageVo.getGroup_id(), notice.getCreatorId());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
                    builder.append("【通知信息】\n")
                            .append("创建人：")
                            .append(card)
                            .append("\n")
                            .append("创建日期：")
                            .append(notice.getDateBegin().format(formatter))
                            .append("\n")
                            .append("终止日期：")
                            .append(notice.getDateEnd().format(formatter))
                            .append("\n")
                            .append("通知内容：")
                            .append(notice.getNoticeText());
                } else {
                    builder.append("当前暂无通知");
                }
                break;
            }
            case "通知删除": {
                if (senderService.isAdmin(messageVo.getGroup_id(), messageVo.getUser_id())) {
                    builder.append(noticeService.delNotice(messageVo));
                } else {
                    builder.append("删除通知需要管理员权限");
                }
                break;
            }
            case "本周运势": {
                builder.append(appDivinationService.weekSingleStatic(messageVo));
                break;
            }
            case "历史运势": {
                builder.append(appDivinationService.historyStatic(messageVo));
                break;
            }
            case "全服成就图鉴": {
                builder.append(achievementService.getAllAchievementPublic());
                break;
            }
            case "成就图鉴": {
                builder.append(achievementService.getAllAchievement(messageVo.getGroup_id()));
                break;
            }
            case "未得成就": {
                builder.append(achievementService.getNotWonAchieve(messageVo.getUser_id()));
                break;
            }
            case "成就": {
                builder.append(achievementService.getOnesAchieve(messageVo.getUser_id()));
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
            case "求签统寄": {
                builder.append(appDivinationService.groupBadStatics(messageVo));
                break;
            }
            case "求签": {
                builder.append(appDivinationService.beginDivination(messageVo));
                break;
            }
            default: {
                return;
            }
        }
        if (builder.toString().equals("")) {
            return;
        }
        GroupNotice notice = noticeDao.getNoticeById(messageVo.getGroup_id(), LocalDate.now());
        if (notice != null && !builder.toString().startsWith("【通知信息】")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
            builder.append("\n本群通知:\n");
            String card = senderService.getCard(messageVo.getGroup_id(), notice.getCreatorId());
            builder.append("【")
                    .append(card)
                    .append("】")
                    .append(notice.getNoticeText());
        }
        if (!botAction) {
            builder.append("\n").append(eduService.queryNow());
        }
        senderService.sendGroup(messageVo.getUser_id(), messageVo.getGroup_id(), builder.toString());
    }

}
