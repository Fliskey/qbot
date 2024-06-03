package com.bot.qspring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bot.qspring.entity.po.Partygroup;
import com.bot.qspring.mapper.PartygroupMapper;
import com.bot.qspring.service.dbauto.IPartygroupService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Fliskey
 * @since 2022-11-06
 */
@Service
public class PartygroupServiceImpl extends ServiceImpl<PartygroupMapper, Partygroup> implements IPartygroupService {

}
