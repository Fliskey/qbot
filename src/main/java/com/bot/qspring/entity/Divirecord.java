package com.bot.qspring.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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


}
