package com.bot.qspring.model.Vo;

import com.bot.qspring.model.Bo.MemberInfo;
import com.bot.qspring.model.Bo.Message;
import com.bot.qspring.model.Bo.Sender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageVo {

    private String post_type;
    private Long self_id;
    private Long user_id;
    private Long target_id;

    private Sender sender;

    private List<Message> message;
    private Long message_id;
    private Long message_seq;
    private String message_type;
    private String raw_message;

    private Long font;
    private String anonymous;
    private Long group_id;

    private Long time;
    private String sub_type;

    public String getMessage(){
        return message.get(0).getData().getText();
    }
}
