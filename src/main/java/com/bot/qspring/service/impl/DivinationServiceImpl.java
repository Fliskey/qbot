package com.bot.qspring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bot.qspring.entity.po.Divination;
import com.bot.qspring.mapper.DivinationMapper;
import com.bot.qspring.service.dbauto.DivinationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author root
 * @since 2022-04-09
 */
@Service
public class DivinationServiceImpl extends ServiceImpl<DivinationMapper, Divination> implements DivinationService {

}
