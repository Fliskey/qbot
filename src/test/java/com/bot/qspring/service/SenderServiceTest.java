package com.bot.qspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SenderServiceTest {

    @Autowired
    SenderService senderService;

    @Test
    void isAdmin() {
        senderService.isAdmin(887452533L, 2214106974L);
    }
}
