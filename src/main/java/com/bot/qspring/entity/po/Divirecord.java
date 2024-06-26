package com.bot.qspring.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author root
 * @since 2022-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("diviRecord")
public class Divirecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long Id;

    private Integer rankingNum;

    private LocalDate lastTime;

    private Integer cumulate;

    private Integer continuity;

    private String lastText;

    private Long diviGroup;

    private Timestamp diviTime;

    private String diviRanking;

    private String lastLevel;

    private String lastGood;

    private String lastBad;

    private Integer bigGoodDays;

    private Integer bigBadDays;

}
