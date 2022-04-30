package com.bot.qspring.dao;

import com.bot.qspring.model.Bo.AchievementAll;
import org.apache.ibatis.annotations.Select;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface AchievementDao {
    @Select("SELECT ac.*," +
            "       IF(" +
            "           count(ac.id) = ( SELECT count(ar3.id) FROM achieve_record ar3 ), 0, count(ac.id) ) cn " +
            "FROM achievement ac, achieve_record ar " +
            "WHERE " +
            "      ac.id = ar.achievement_id " +
            "   or ac.id not in ( SELECT ar2.achievement_id FROM achieve_record ar2 )" +
            "GROUP BY ac.id " +
            "ORDER BY cn desc "
            )
    public List<AchievementAll> getAllOwnedAchievement();


    @Select("SELECT dh.*, count(DISTINCT dh.id, ar3.user_id) cn " +
            "FROM ( " +
            "    SELECT ac.* " +
            "    FROM achievement ac, achieve_record ar " +
            "    WHERE ac.id not in( " +
            "        SELECT ar2.achievement_id " +
            "        FROM achieve_record ar2 " +
            "        WHERE ar2.user_id = #{userId} " +
            "        ) " +
            "    ) dh " +
            "    LEFT JOIN achieve_record ar3 " +
            "        ON dh.id = ar3.achievement_id " +
            "GROUP BY dh.id, is_custom " +
            "ORDER BY is_custom DESC, cn DESC ")
    public List<AchievementAll> getNotWon(Long userId);

}
