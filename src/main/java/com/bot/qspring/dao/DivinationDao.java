package com.bot.qspring.dao;

import com.bot.qspring.entity.Divination;
import com.bot.qspring.mapper.DivinationMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivinationDao {
    @Select("SELECT * " +
            "FROM divination " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    public Divination getRandDivination();

    @Select("SELECT *" +
            "FROM divination di " +
            "WHERE di.meme_from is not null")
    public List<Divination> getDiviHasMeme();
}
