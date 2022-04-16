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
 * @since 2022-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("diviGroupRecord")
public class Divigrouprecord implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

    private LocalDate lastTime;

    private Integer num;


}
