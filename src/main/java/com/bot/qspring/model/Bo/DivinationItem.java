package com.bot.qspring.model.Bo;

import lombok.Data;

import java.util.List;

@Data
public class DivinationItem {
    private Long id;
    private String type;
    private String title;
    private String content;
    private Long conflict;
    private Long only_user;
    private Long only_group;
}
