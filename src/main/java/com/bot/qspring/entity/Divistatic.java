package com.bot.qspring.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author root
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("diviStatic")
public class Divistatic implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

    @TableField("weekRecord")
    private Integer weekrecord;

    @TableField("tLevel1")
    private Integer tlevel1;

    @TableField("tLevel2")
    private Integer tlevel2;

    @TableField("tLevel3")
    private Integer tlevel3;

    @TableField("tLevel4")
    private Integer tlevel4;

    @TableField("tLevel5")
    private Integer tlevel5;


}
