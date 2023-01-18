package com.bot.qspring.service;

import com.bot.qspring.model.Vo.MessageVo;
import org.springframework.stereotype.Service;

@Service
public class AppDiceService {

    String diceEnter(MessageVo messageVo){
        String msg = messageVo.getMessage();
        return null;
    }
}
