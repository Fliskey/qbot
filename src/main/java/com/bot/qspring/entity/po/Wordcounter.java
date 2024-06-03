package com.bot.qspring.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
