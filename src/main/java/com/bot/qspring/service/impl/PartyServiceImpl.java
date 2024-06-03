package com.bot.qspring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bot.qspring.entity.po.Party;
import com.bot.qspring.mapper.PartyMapper;
import com.bot.qspring.service.dbauto.PartyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author root
 * @since 2022-04-16
 */
@Service
public class PartyServiceImpl extends ServiceImpl<PartyMapper, Party> implements PartyService {

}
