package com.bot.qspring.dao;

import com.bot.qspring.entity.po.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EduDao {
    @Select("SELECT * " +
            "FROM edu_record " +
            "ORDER BY id DESC " +
            "LIMIT 1")
    public EduRecord getLastRecord();

    @Select("SELECT * " +
            "FROM edu_dish " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    public EduDish getRandDish();

    @Select("SELECT * " +
            "FROM edu_book " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    public EduBook getRandBook();

    @Select("SELECT * " +
            "FROM edu_movie " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    public EduMovie getRandMovie();

    @Select("SELECT * " +
            "FROM edu_sport " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    public EduSport getRandSport();

    @Select("SELECT * " +
            "FROM edu_record " +
            "WHERE start_time between #{today} and #{tomorrow}")
    public List<EduRecord> getRecodeListByDay(@Param("today") LocalDate today, @Param("tomorrow") LocalDate tomorrow);
}
