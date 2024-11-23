package site.mufen.domain.activity.service.quota;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.mufen.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import site.mufen.domain.activity.model.entity.*;
import site.mufen.domain.activity.model.valobj.OrderTradeTypeVO;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.domain.activity.service.IRaffleActivityAccountQuotaService;
import site.mufen.domain.activity.service.quota.policy.ITradePolicy;
import site.mufen.domain.activity.service.quota.rule.IActionChain;
import site.mufen.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author mufen
 * @Description 抽奖活动抽象类 定义标准的流程
 * @create 2024/11/1 19:47
 */
@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {

    private final Map<String, ITradePolicy> tradePolicyMap;


    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, Map<String, ITradePolicy> tradePolicyGroup) {
        super(activityRepository, defaultActivityChainFactory);
        this.tradePolicyMap = tradePolicyGroup;
    }


    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        // 1. 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        // 2. 查询活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 3. 查询次数信息（用户在活动上可参与的次数）
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }

    @Override
    public UnpaidActivityOrderEntity createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        // 1.参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 1.2. 查询是否有一个月以内的未支付订单
        UnpaidActivityOrderEntity unpaidActivityOrderEntity = activityRepository.queryUnpaidActivityOrderWithinOneMonth(skuRechargeEntity);
        if (null != unpaidActivityOrderEntity) {
            return unpaidActivityOrderEntity;
        }
        // 2.查询基础信息
        // 2.1 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        // 2.2 查询活动信息
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 2.3 查询次数信息
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        if (skuRechargeEntity.getOrderTradeTypeVO().equals(OrderTradeTypeVO.credit_pay_trade)) {
            BigDecimal userCreditAmount = activityRepository.queryUserCreditAccountAmount(userId);
            if (userCreditAmount.compareTo(activitySkuEntity.getProductAmount()) < 0) {
                throw new AppException(ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getCode(), ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getInfo());
            }
        }
        // 3. 活动动作规则校验 todo 后续处理规则过滤流程 暂时不处理细节
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);
        // 4. 构建订单聚合对象
        CreateQuotaOrderAggregate createQuotaOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);
        // 5. 保存订单
        ITradePolicy tradePolicy = tradePolicyMap.get(skuRechargeEntity.getOrderTradeTypeVO().getCode());
        tradePolicy.trade(createQuotaOrderAggregate);
        // 6. 返回订单
        ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrderEntity();
        return UnpaidActivityOrderEntity.builder()
            .userId(activityOrderEntity.getUserId())
            .orderId(activityOrderEntity.getOrderId())
            .outBusinessNo(activityOrderEntity.getOutBusinessNo())
            .payAmount(activityOrderEntity.getPayAmount())
            .build();
    }

    protected abstract void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
