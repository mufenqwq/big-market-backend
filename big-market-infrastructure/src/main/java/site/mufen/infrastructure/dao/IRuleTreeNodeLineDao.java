package site.mufen.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.dao.po.RuleTreeNodeLine;

import java.util.List;

/**
 * @author mufen
 * @Description 规则树节点线[from -> to]
 * @create 2024/10/22 09:27
 */
@Mapper
public interface IRuleTreeNodeLineDao {

    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);
}
