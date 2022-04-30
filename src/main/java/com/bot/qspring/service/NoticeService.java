package com.bot.qspring.service;

import com.bot.qspring.entity.Groupnotice;
import com.bot.qspring.mapper.GroupnoticeMapper;
import com.bot.qspring.model.Vo.MessageVo;
import com.bot.qspring.service.dbauto.GroupnoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NoticeService {
    @Autowired
    private SenderService senderService;

    @Autowired
    private GroupnoticeService groupnoticeService;

    @Autowired
    private GroupnoticeMapper groupnoticeMapper;

    public String addNotice(MessageVo vo){
        String msg = vo.getMessage();
        String[] cmdList = msg.split(" ");
        String help = "【求签附加通知】\n命令格式：\n通知 群号 通知信息 开始日期 持续天数\n日期按MMDD四位数格式输入";
        try{
            if(cmdList.length != 5){
                throw new Exception();
            }
            Long groupId = Long.valueOf(cmdList[1]);
            String noticeMsg = cmdList[2];
            int dateNum = Integer.parseInt(cmdList[3]);
            int MM = dateNum / 100;
            int DD = dateNum % 100;
            LocalDate startDate = LocalDate.of(LocalDate.now().getYear(),MM,DD);
            long durDay = Long.parseLong(cmdList[4]);
            //不包括
            LocalDate endDate = startDate.plusDays(durDay);

            //TODO 此处应有验证
            Groupnotice groupnotice = new Groupnotice();
            groupnotice.setGroupId(groupId);
            groupnotice.setCreatorId(vo.getUser_id());
            groupnotice.setNoticeText(noticeMsg);
            groupnotice.setDateBegin(startDate);
            groupnotice.setDateEnd(endDate);

            groupnoticeService.save(groupnotice);
        }
        catch (Exception e){
            return "命令格式错误！\n\n" + help;
        }
        return "";
    }
}
