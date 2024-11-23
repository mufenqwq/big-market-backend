package site.mufen.domain.activity.service.quota.policy;

import site.mufen.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @author mufen
 * @Description 交易策略接口
 * @create 2024/11/14 21:42
 */
public interface ITradePolicy {
    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
