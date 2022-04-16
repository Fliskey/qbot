package com.bot.qspring.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author root
 * @since 2022-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wordCounter")
public class Wordcounter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String word;

    private LocalDate date;

    private Integer counter;

    private Boolean reject;

    private LocalDateTime stopUntil;


}
