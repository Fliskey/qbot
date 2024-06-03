package com.bot.qspring.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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

    private LocalTime onTime;

    private LocalTime offTime;


}
