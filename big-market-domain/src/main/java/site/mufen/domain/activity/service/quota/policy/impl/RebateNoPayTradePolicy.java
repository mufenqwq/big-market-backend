package site.mufen.domain.activity.service.quota.policy.impl;

import org.springframework.stereotype.Service;
import site.mufen.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import site.mufen.domain.activity.model.valobj.OrderStateVO;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.domain.activity.service.quota.policy.ITradePolicy;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 21:46
 */
@Service("rebate_no_pay_trade")
public class RebateNoPayTradePolicy implements ITradePolicy {
    private final IActivityRepository activityRepository;

    public RebateNoPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        // 不需要充值，则将订单金额修改为0,订单状态修改为完成，直接给用户充值
        createQuotaOrderAggregate.setOrderState(OrderStateVO.completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveCreditNoPayOrder(createQuotaOrderAggregate);

    }
}
