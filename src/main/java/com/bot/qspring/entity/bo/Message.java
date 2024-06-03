package com.bot.qspring.entity.bo;

import lombok.Data;

@Data
public class Message {

    public class Data {
        String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    Data data;
    String type;
}
