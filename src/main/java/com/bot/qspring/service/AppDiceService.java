package com.bot.qspring.service;

import com.bot.qspring.model.Vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppDiceService {

    private final SenderService senderService;

    @Autowired
    AppDiceService(SenderService senderService) {
        this.senderService = senderService;
    }

    String diceEnter(MessageVo messageVo) {
        String msg = messageVo.getMessage();
        Long userId = messageVo.getUser_id();

        //去掉开头的.
        msg = msg.substring(1);

        /**
         *
         */

        return null;
    }
}
