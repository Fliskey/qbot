package com.bot.qspring.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@TableName("bookXmuSmyz")
public class Bookxmusmyz implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String gander;

    private String major;

    private String college;

    private Integer grade;

    private String tel;


}
