package com.bot.qspring.service;

import com.bot.qspring.entity.ServiceSwitcher;
import com.bot.qspring.model.Vo.MessageVo;
import com.bot.qspring.service.dbauto.ServiceSwitcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AppSwitcherService {

    @Autowired
    ServiceSwitcherService serviceSwitcherService;

    @Autowired
    SenderService senderService;

    public String switchJudge(ServiceSwitcher serviceSwitcher, MessageVo messageVo){
        if(serviceSwitcher.getStartDay() == null ||
                serviceSwitcher.getStartDay().isAfter(LocalDate.now())){
            if(messageVo.getMessage().contains("bot") && messageVo.getMessage().contains("on")){
                switchOn(serviceSwitcher);
                if(!senderService.isAdmin(messageVo.getGroup_id(), messageVo.getUser_id())){
                    return "Off";
                }
                return "已开启bot";
            }
            return "Off";
        }
        else{
            if(!senderService.isAdmin(messageVo.getGroup_id(), messageVo.getUser_id())){
                return "Off";
            }
            switchOn(serviceSwitcher);
            return "定时已到，已开启bot";
        }
    }

    public void switchOn(ServiceSwitcher serviceSwitcher){
        serviceSwitcher.setIdStopped(false);
        serviceSwitcher.setStartDay(null);
        serviceSwitcherService.updateById(serviceSwitcher);
    }

    public String switchOff(MessageVo messageVo){
        if(!senderService.isAdmin(messageVo.getGroup_id(), messageVo.getUser_id())){
            return "关bot需要管理员权限~";
        }
        StringBuilder builder = new StringBuilder();
        ServiceSwitcher serviceSwitcher = new ServiceSwitcher();
        serviceSwitcher.setId(messageVo.getGroup_id());
        serviceSwitcher.setIdStopped(true);
        serviceSwitcher.setStartDay(null);
        builder.append("bot告退");
        String msg = messageVo.getMessage();
        if(msg.contains("天")){
            try{
                int DayLocate = msg.indexOf("天");
                String DuringDayChar = msg.substring(DayLocate-1, DayLocate);
                int duringDay = Integer.parseInt(DuringDayChar);
                serviceSwitcher.setStartDay(LocalDate.now().plusDays(duringDay));
                builder
                        .append("，已设置")
                        .append(duringDay)
                        .append("天后自动开启");
            } catch (Exception e){
                builder.append("，重启日期设置失败，需要手动开启哦");
            }
        }
        else{
            builder.append("，未设置自动重启");
        }
        serviceSwitcherService.saveOrUpdate(serviceSwitcher);
        return builder.toString();
    }
}
