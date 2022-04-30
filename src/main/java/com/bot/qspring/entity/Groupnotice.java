package com.bot.qspring.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author root
 * @since 2022-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("groupNotice")
public class Groupnotice implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long creatorId;

    private Long groupId;

    private String noticeText;

    private LocalDate dateEnd;

    private LocalDate dateBegin;


}
