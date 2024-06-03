package com.bot.qspring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bot.qspring.entity.po.ServiceSwitcher;
import com.bot.qspring.mapper.ServiceSwitcherMapper;
import com.bot.qspring.service.dbauto.ServiceSwitcherService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Fliskey
 * @since 2022-11-27
 */
@Service
public class ServiceSwitcherServiceImpl extends ServiceImpl<ServiceSwitcherMapper, ServiceSwitcher> implements ServiceSwitcherService {

}
