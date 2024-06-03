package com.bot.qspring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bot.qspring.entity.po.Achievement;
import com.bot.qspring.mapper.AchievementMapper;
import com.bot.qspring.service.dbauto.AchievementService;
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
public class AchievementServiceImpl extends ServiceImpl<AchievementMapper, Achievement> implements AchievementService {

}
