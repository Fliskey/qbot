package com.bot.qspring.entity.bo;

import lombok.Data;

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
