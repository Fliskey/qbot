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
