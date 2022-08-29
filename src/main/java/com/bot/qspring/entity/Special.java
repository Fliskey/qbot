package com.bot.qspring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Special implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String type;

    private String reason;

    @TableField("specialDay")
    private LocalDate specialday;

    private String text;


}
