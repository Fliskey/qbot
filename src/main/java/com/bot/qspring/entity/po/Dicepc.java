package com.bot.qspring.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("dicePC")
public class Dicepc implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long plId;

    private String name;

    private LocalDate regDate;


}
