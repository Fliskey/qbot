package com.bot.qspring.entity;

import java.time.LocalDate;
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
public class ServiceSwitcher implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Boolean idStopped;

    private LocalDate startDay;


}
