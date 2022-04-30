package com.bot.qspring.model.Bo;

import lombok.Data;

@Data
public class AchievementAll {
    private Long id;

    private String achieveName;

    private String detail;

    private Long firstWon;

    private Boolean isCustom;

    private Long cn;
}
