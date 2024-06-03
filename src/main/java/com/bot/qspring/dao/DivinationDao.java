package com.bot.qspring.dao;

import com.bot.qspring.entity.po.Divination;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivinationDao {
    @Select("SELECT * " +
            "FROM divination " +
            "WHERE special_id is null " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    public Divination getRandDivination();

    @Select("SELECT * " +
            "FROM divination " +
            "WHERE special_id = #{sid} " +
            "ORDER BY RAND() " +
            "LIMIT 1 ")
    public Divination getSpecialDivination(@Param("sid") Integer sid);

    @Select("SELECT *" +
            "FROM divination di " +
            "WHERE di.meme_from is not null")
    public List<Divination> getDiviHasMeme();

}
