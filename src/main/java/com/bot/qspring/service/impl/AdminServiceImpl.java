package com.bot.qspring.service.impl;

import com.bot.qspring.entity.Admin;
import com.bot.qspring.mapper.AdminMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.bot.qspring.service.dbauto.AdminService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2022-04-23
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
