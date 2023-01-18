package com.bot.qspring.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("diviRecordDetail")
public class DiviRecordDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long diviUser;

    private Long diviGroup;

    private LocalDateTime diviTime;

    private String diviLevel;

    private String diviText;

    private Integer cumulate;

    private Integer continuity;

    public DiviRecordDetail(Divirecord record){
        diviUser = record.getId();
        diviGroup = record.getDiviGroup();
        diviText = record.getLastText();
        diviLevel = record.getLastLevel();
        diviTime = LocalDateTime.now();
        continuity = record.getContinuity();
        cumulate = record.getCumulate();
    }


}
