package com.bot.qspring.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author Fliskey
 * @since 2022-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("partyGroup")
public class Partygroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("partyId")
    private Integer partyid;

    @TableField("groupId")
    private Long groupid;


}
