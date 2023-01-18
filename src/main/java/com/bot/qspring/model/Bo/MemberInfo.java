package com.bot.qspring.model.Bo;

import lombok.Data;

@Data
public class MemberInfo {
    private Long group_id;
    private Long user_id;
    private String nickname;
    private String card;
    private String sex;
    private String role;
    private String title;
    private Long join_time;
    private Long last_sent_time;
}
