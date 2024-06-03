package com.bot.qspring.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author Fliskey
 * @since 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dicePL")
public class Dicepl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * QQ号
     */
    private Long id;

    private Integer nowPc;

    /**
     * 注册时间
     */
    private LocalDate regDate;

    /**
     * 注册群
     */
    private Long regGroup;


}
