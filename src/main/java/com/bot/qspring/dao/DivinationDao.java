package com.bot.qspring.dao;

import com.bot.qspring.entity.Divination;
import com.bot.qspring.mapper.DivinationMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public interface DivinationDao {
    @Select("SELECT * " +
            "FROM divination " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    public Divination getRandDivination();
}
