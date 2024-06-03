package com.bot.qspring.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("replaceWord")
public class Replaceword implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String wordBefore;

    private String wordAfter;


}
