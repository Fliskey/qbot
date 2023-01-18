package com.bot.qspring.dao;

import com.bot.qspring.entity.GroupNotice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public interface NoticeDao {
    @Select("SELECT * " +
            "FROM groupNotice " +
            "WHERE id = #{groupId} AND " +
            "      date(#{today}) BETWEEN date_begin AND date_end AND" +
            "      is_deleted = 0")
    public GroupNotice getNoticeById(@Param("groupId") Long groupId, @Param("today") LocalDate today);

}
