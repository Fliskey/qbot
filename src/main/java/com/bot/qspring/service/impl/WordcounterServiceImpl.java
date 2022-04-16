package com.bot.qspring.service.impl;

import com.bot.qspring.entity.Wordcounter;
import com.bot.qspring.mapper.WordcounterMapper;
import com.bot.qspring.service.dbauto.WordcounterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2022-04-16
 */
@Service
public class WordcounterServiceImpl extends ServiceImpl<WordcounterMapper, Wordcounter> implements WordcounterService {

}
