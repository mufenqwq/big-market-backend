package site.mufen.infrastructure.persistent.dao.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mufen
 * @Description 日常行为返利活动配置 持久化对象
 * @create 2024/11/8 20:49
 */
@Data
public class DailyBehaviorRebate {

    /**
     * 自增Id
     */
    private Long id;
    /**
     * 行为类型(sign-签到，openai_pay 支付)
     */
    private String behaviorType;
    /**
     * 返利描述
     */
    private String rebateDesc;
    /**
     * 返利类型 (sku 返利库存充值商品，integral 用户活动积分)
     */
    private String rebateType;
    /**
     *  返利配置
     */
    private String rebateConfig;
    /**
     * 状态 (open-开启 close-结束)
     */
    private String state;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 结束时间
     */
    private Date updateTime;
}
