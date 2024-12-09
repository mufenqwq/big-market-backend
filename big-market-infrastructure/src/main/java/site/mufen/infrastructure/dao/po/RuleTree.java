package site.mufen.infrastructure.persistent.dao.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mufen
 * @Description 规则树
 * @create 2024/10/22 08:43
 */
@Data
public class RuleTree {
    /**
     * 自增Id
     */
    private Long id;
    /**
     * 规则树id
     */
    private String treeId;
    /**
     * 规则树名称
     */
    private String treeName;
    /**
     * 规则树描述
     */
    private String treeDesc;
    /** 规则根节点 */
    private String treeRootRuleKey;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
