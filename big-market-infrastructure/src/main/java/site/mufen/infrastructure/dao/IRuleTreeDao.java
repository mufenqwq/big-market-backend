package site.mufen.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.dao.po.RuleTree;

/**
 * @author mufen
 * @Description 规则树Dao
 * @create 2024/10/22 09:10
 */
@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String TreeId);
}
