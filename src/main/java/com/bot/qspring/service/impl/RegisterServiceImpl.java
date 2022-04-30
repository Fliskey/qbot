package com.bot.qspring.service.impl;

import com.bot.qspring.entity.Register;
import com.bot.qspring.mapper.RegisterMapper;
import com.bot.qspring.service.dbauto.RegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2022-04-30
 */
@Service
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper, Register> implements RegisterService {

}
