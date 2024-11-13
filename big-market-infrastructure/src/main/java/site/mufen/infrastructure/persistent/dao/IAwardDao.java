package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.Award;

import java.util.Date;
import java.util.List;

/**
 * @author mufen
 * @Description 奖品表 dao
 * @create 2024/10/15 15:15
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();

    String queryAwardConfigByAwardId(Integer awardId);

    String queryAwardKeyByAwardId(Integer awardId);
}
