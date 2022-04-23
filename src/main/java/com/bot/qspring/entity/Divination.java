package com.bot.qspring.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author root
 * @since 2022-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Divination implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer id;

    private String type;

    private String title;

    private String content;

    private String key;

    private String memeFrom;

    private Integer conflict;

    private Long onlyUser;

    private Long onlyGroup;

    private String tag;


}
