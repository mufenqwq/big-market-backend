package site.mufen.domain.credit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.mufen.domain.credit.event.CreditAdjustSuccessMessageEvent;
import site.mufen.domain.credit.model.aggregate.TradeAggregate;
import site.mufen.domain.credit.model.entity.CreditAccountEntity;
import site.mufen.domain.credit.model.entity.CreditOrderEntity;
import site.mufen.domain.credit.model.entity.TaskEntity;
import site.mufen.domain.credit.model.entity.TradeEntity;
import site.mufen.domain.credit.repository.ICreditRepository;
import site.mufen.types.event.BaseEvent;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author mufen
 * @Description 积分调整服务
 * @create 2024/11/14 00:32
 */
@Service
@Slf4j
public class CreditAdjustService implements ICreditAdjustService{

    @Resource
    private ICreditRepository creditRepository;
    @Resource
    private CreditAdjustSuccessMessageEvent creditAdjustSuccessMessageEvent;

    @Override
    public String createOrder(TradeEntity tradeEntity) {
        log.info("用户积分额度开始 userId:{}, tradeName:{}, tradeType:{}, amount:{}", tradeEntity.getUserId(), tradeEntity.getTradeNameVO(), tradeEntity.getTradeTypeVO(), tradeEntity.getTradeAmount());
        // 1. 创建用户积分账户
        CreditAccountEntity creditAccountEntity = TradeAggregate.createCreditAccountEntity(tradeEntity.getUserId(), tradeEntity.getTradeAmount());

        // 2. 创建账户订单实体
        CreditOrderEntity creditOrderEntity = TradeAggregate.createCreditOrderEntity(
            tradeEntity.getUserId(),
            tradeEntity.getTradeNameVO(),
            tradeEntity.getTradeTypeVO(),
            tradeEntity.getTradeAmount(),
            tradeEntity.getOutBusinessNo()
        );

        // 构建消息对象
        CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = new CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage();
        creditAdjustSuccessMessage.setUserId(tradeEntity.getUserId());
        creditAdjustSuccessMessage.setOrderId(creditOrderEntity.getOrderId());
        creditAdjustSuccessMessage.setAmount(tradeEntity.getTradeAmount());
        creditAdjustSuccessMessage.setOutBusinessNo(tradeEntity.getOutBusinessNo());
        BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> creditAdjustSuccessMessageEventMessage = creditAdjustSuccessMessageEvent.buildEventMessage(creditAdjustSuccessMessage);


        // 3.构建交易聚合对象
        TaskEntity taskEntity = TradeAggregate.createTaskEntity(tradeEntity.getUserId(), creditAdjustSuccessMessageEvent.topic(), creditAdjustSuccessMessageEventMessage.getId(), creditAdjustSuccessMessageEventMessage);
        TradeAggregate tradeAggregate = TradeAggregate.builder()
            .userId(tradeEntity.getUserId())
            .creditOrderEntity(creditOrderEntity)
            .creditAccountEntity(creditAccountEntity)
            .taskEntity(taskEntity).build();

        // 4. 保存用户积分订单和账户修改
        creditRepository.saveUserCreditTradeOrder(tradeAggregate);



        log.info("增加用户积分额度完成，userId:{}, orderId:{}", tradeEntity.getUserId(), creditOrderEntity.getOrderId());
        return creditOrderEntity.getOrderId();
    }

    /**
     * 查询用户积分账户
     * @param userId 用户Id
     * @return 用户积分账户
     */
    @Override
    public CreditAccountEntity queryUserCreditAccount(String userId) {
        return creditRepository.queryUserCreditAccount(userId);
    }
}
