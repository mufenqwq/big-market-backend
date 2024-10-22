package site.mufen.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 规则树节点指向线对象，用于衔接 from -> to 节点链路关系
 * @create 2024/10/21 15:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeLineVO {

    /**
     * 规则树ID
     */
    private String treeId;
    /**
     * 规则key节点 from
     */
    private String ruleNodeFrom;
    /**
     * 规则key节点 to
     */
    private String ruleNodeTo;
    /**
     * 限定类型 1:= 2:> 3:< 4:>= 5<= 6:enum枚举范围
     */
    private RuleLimitTypeVO ruleLimitType;
    /**
     * 限定值(到下一个节点
     */
    private RuleLogicCheckTypeVO ruleLimitValue;
}
