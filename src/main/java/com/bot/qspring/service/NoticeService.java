package com.bot.qspring.service;

import com.bot.qspring.entity.GroupNotice;
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
        try{
            if(cmdList.length == 1 || cmdList.length > 3){
                return "命令格式错误！通知示例：\n通知 村口集合，水泥自带 持续2天\n(不写则默认为一天)";
            }
            LocalDate startDate = LocalDate.now();

            String noticeMsg = cmdList[1];
            //默认持续时间为一天
            LocalDate endDate = LocalDate.now().plusDays(1);
            int addDay = 1;
            if(cmdList.length == 3){
                String duringMsg = cmdList[2];
                int dayIndex = duringMsg.indexOf("天");
                if(dayIndex != -1){
                    String addDayChar = duringMsg.substring(dayIndex-1, dayIndex);
                    addDay = Integer.parseInt(addDayChar);
                    endDate = endDate.plusDays(addDay);
                }
            }

            GroupNotice groupnotice = new GroupNotice();
            groupnotice.setId(vo.getGroup_id());
            groupnotice.setCreatorId(vo.getUser_id());
            groupnotice.setNoticeText(noticeMsg);
            groupnotice.setDateBegin(startDate);
            groupnotice.setDateEnd(endDate);
            groupnotice.setIsDeleted(false);

            groupnoticeService.saveOrUpdate(groupnotice);
            return "保存成功，持续通知" + addDay + "天";
        }
        catch (NumberFormatException e){
            return "时间格式错误";
        }
    }

    public String delNotice(MessageVo vo){
        GroupNotice groupNotice = groupnoticeService.getById(vo.getGroup_id());
        if(groupNotice == null){
            return "现无有效通知";
        }
        else{
            groupNotice.setIsDeleted(true);
            groupnoticeService.updateById(groupNotice);
            return "删除成功";
        }
    }
}
