package com.bot.qspring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bot.qspring.entity.po.GroupNotice;
import com.bot.qspring.mapper.GroupnoticeMapper;
import com.bot.qspring.service.dbauto.GroupnoticeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author root
 * @since 2022-04-24
 */
@Service
public class GroupnoticeServiceImpl extends ServiceImpl<GroupnoticeMapper, GroupNotice> implements GroupnoticeService {

}
