package site.mufen.domain.activity.service.quota.policy.impl;

import org.springframework.stereotype.Service;
import site.mufen.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import site.mufen.domain.activity.model.valobj.OrderStateVO;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.domain.activity.service.quota.policy.ITradePolicy;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 21:46
 */
@Service("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
            createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
            activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }
}
