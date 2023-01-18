package com.bot.qspring.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
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
public class GroupNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long creatorId;

    private Long id;

    private String noticeText;

    private LocalDate dateEnd;

    private LocalDate dateBegin;

    private Boolean isDeleted;


}
