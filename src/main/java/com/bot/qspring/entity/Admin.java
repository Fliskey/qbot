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
 * @since 2022-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;


}
