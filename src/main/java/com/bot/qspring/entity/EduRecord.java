package com.bot.qspring.entity;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author Fliskey
 * @since 2024-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EduRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long op;

    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime endTime;

    private String type;

    private String name;

    private Integer eduId;


}
