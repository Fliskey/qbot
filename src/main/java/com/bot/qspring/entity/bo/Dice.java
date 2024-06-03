package com.bot.qspring.entity.bo;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;

@Data
public class Dice {
    private int range = 100;
    private int val;

    public Dice(int setRange) {
        this.range = setRange;
    }

    public Dice roll() {
        val = RandomUtil.randomInt(1, range);
        return this;
    }
}
