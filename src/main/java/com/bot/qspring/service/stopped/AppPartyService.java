package com.bot.qspring.service.stopped;

import com.bot.qspring.entity.po.Bookxmusmyz;
import com.bot.qspring.entity.po.Party;
import com.bot.qspring.entity.po.Partysignup;
import com.bot.qspring.entity.vo.MessageVo;
import com.bot.qspring.mapper.PartyMapper;
import com.bot.qspring.mapper.PartysignupMapper;
import com.bot.qspring.service.dbauto.BookxmusmyzService;
import com.bot.qspring.service.dbauto.PartyService;
import com.bot.qspring.service.dbauto.PartysignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppPartyService {
    @Autowired
    private BookxmusmyzService bookxmusmyzService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyMapper partyMapper;

    @Autowired
    private PartysignupService partysignupService;

    @Autowired
    private PartysignupMapper partysignupMapper;

    public String staticsParty(MessageVo vo) {
        String msg = vo.getMessage();
        String[] msgList = msg.split(" ");
        if (msgList.length <= 1) {
            return "您要统计哪个聚会？";
        }
        String partyName = msgList[1];
        Map<String, Object> map = new HashMap<>();
        map.put("party_name", partyName);
        List<Party> partyList = partyMapper.selectByMap(map);
        if (partyList.size() == 0) {
            return "没有这个名字叫【" + partyName + "】的聚会哦~";
        }
        Integer partyId = partyList.get(0).getId();
        map.clear();
        map.put("party_id", partyId);
        map.put("sign_up_group", vo.getGroup_id());
        List<Partysignup> partysignups = partysignupMapper.selectByMap(map);
        if (partysignups.size() == 0) {
            return "本群中未找到报名名为【" + partyName + "】的同学哦~";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("报名了【" + partyName + "】活动的同学有：\n");
        if (vo.getGroup_id().equals(438851137L)) {
            List<Bookxmusmyz> bookxmusmyzs = toBookList(partysignups);
            for (Bookxmusmyz fellow : bookxmusmyzs) {
                builder
                        .append(fellow.getGrade())
                        .append("级")
                        .append(fellow.getCollege())
                        .append(" ")
                        .append(fellow.getName())
                        .append("\n");
            }
        } else {
            partysignups.sort(Comparator.comparing(Partysignup::getCard));
            for (Partysignup partysignup : partysignups) {
                builder.append(partysignup.getCard()).append("\n");
            }
        }
        builder.append("共有【" + partysignups.size() + "】名同学参加\n");
        return builder.toString();
    }

    public String noticeParty(MessageVo vo) {
        String msg = vo.getMessage();
        String[] msgList = msg.split(" ");
        if (msgList.length <= 1) {
            return "格式错误";
        }
        String partyName = msgList[1];
        Map<String, Object> map = new HashMap<>();
        map.put("party_name", partyName);
        List<Party> partyList = partyMapper.selectByMap(map);
        if (partyList.size() == 0) {
            return "无此聚会：" + partyName;
        }
        Integer partyId = partyList.get(0).getId();
        map.clear();
        map.put("party_id", partyId);
        List<Partysignup> partysignups = partysignupMapper.selectByMap(map);
        if (partysignups.size() == 0) {
            return "无人报名";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("已at报名了【" + partyName + "】的同学：");
            if (vo.getGroup_id().equals(438851137L)) {
                List<Bookxmusmyz> bookxmusmyzs = toBookList(partysignups);
                for (Bookxmusmyz fellow : bookxmusmyzs) {
                    builder.append("\n[CQ:at,qq=" + fellow.getId() + "]");
                }
            } else {
                partysignups.sort(Comparator.comparing(Partysignup::getCard));
                for (Partysignup partysignup : partysignups) {
                    builder.append("\n[CQ:at,qq=" + partysignup.getUserId() + "]");
                }
            }
            return builder.toString();
        }

    }

    private List<Bookxmusmyz> toBookList(List<Partysignup> partysignups) {
        List<Bookxmusmyz> bookxmusmyzs = new ArrayList<>();
        for (Partysignup partysignup : partysignups) {
            Long userId = partysignup.getUserId();
            Bookxmusmyz fellow = bookxmusmyzService.getById(userId);
            bookxmusmyzs.add(fellow);
        }
        bookxmusmyzs.sort(Comparator.comparing(Bookxmusmyz::getCollege));
        bookxmusmyzs.sort(Comparator.comparing(Bookxmusmyz::getGrade));
        return bookxmusmyzs;
    }

    public String signUpParty(MessageVo vo) {
        String msg = vo.getMessage();
        String[] msgList = msg.split(" ");
        if (msgList.length <= 1) {
            return "您要报名哪个聚会？";
        }
        String partyName = msgList[1];
        Map<String, Object> map = new HashMap<>();
        map.put("party_name", partyName);
        List<Party> partyList = partyMapper.selectByMap(map);
        if (partyList.size() == 0) {
            Party party = new Party();
            party.setPartyName(partyName);
            party.setCreator(vo.getUser_id());
            partyService.save(party);
            partyList = partyMapper.selectByMap(map);
        }
        Integer partyId = partyList.get(0).getId();
        map.clear();
        map.put("party_id", partyId);
        map.put("user_id", vo.getUser_id());
        List<Partysignup> partysignups = partysignupMapper.selectByMap(map);
        if (partysignups.size() == 0) {
            //未报名
            Partysignup partysignup = new Partysignup();
            partysignup.setSignUpGroup(vo.getGroup_id());
            partysignup.setPartyId(partyId);
            partysignup.setUserId(vo.getUser_id());
            partysignup.setCard(vo.getSender().getCard());
            partysignup.setSignUpTime(LocalDateTime.now());
            partysignupService.save(partysignup);
            return "您已成功报名【" + partyName + "】！";
        } else {
            return "您已报名过该活动，请勿重复报名！";
        }
    }

    public String cancelSignUpParty(MessageVo vo) {
        String msg = vo.getMessage();
        String[] msgList = msg.split(" ");
        if (msgList.length <= 1) {
            return "您要退出哪个聚会？";
        }
        String partyName = msgList[1];
        Map<String, Object> map = new HashMap<>();
        map.put("party_name", partyName);
        List<Party> partyList = partyMapper.selectByMap(map);
        if (partyList.size() == 0) {
            return "没有这个名字叫【" + partyName + "】的聚会哦~";
        }
        Integer partyId = partyList.get(0).getId();
        map.clear();
        map.put("party_id", partyId);
        map.put("user_id", vo.getUser_id());
        List<Partysignup> partysignups = partysignupMapper.selectByMap(map);
        if (partysignups.size() == 0) {
            return "您没有报名【" + partyName + "】哦~";
        } else {
            Partysignup partysignup = partysignups.get(0);
            partysignupService.removeById(partysignup);
            map.clear();
            map.put("party_id", partyId);
            partysignups = partysignupMapper.selectByMap(map);
            if (partysignups.size() == 0) {
                //该活动没人参与了
                partyService.removeById(partyId);
                return "您已成功退出了【" + partyName + "】！\n鉴于该活动已无人报名，现已删除该活动";
            } else {
                return "您已成功退出了【" + partyName + "】！";
            }
        }
    }

}
