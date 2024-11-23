package site.mufen.domain.credit.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import site.mufen.domain.credit.event.CreditAdjustSuccessMessageEvent;
import site.mufen.domain.credit.model.entity.CreditAccountEntity;
import site.mufen.domain.credit.model.entity.CreditOrderEntity;
import site.mufen.domain.credit.model.entity.TaskEntity;
import site.mufen.domain.credit.model.valobj.TradeNameVO;
import site.mufen.domain.credit.model.valobj.TradeTypeVO;
import site.mufen.domain.rebate.model.valobj.TaskStateVO;
import site.mufen.types.event.BaseEvent;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description 交易积分聚合对象
 * @create 2024/11/14 00:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeAggregate {
    // 用户Id
    private String userId;
    // 积分账户实体
    private CreditAccountEntity creditAccountEntity;
    // 积分订单实体
    private CreditOrderEntity creditOrderEntity;
    // 任务实体 - 补偿 MQ消息
    private TaskEntity taskEntity;

    public static CreditAccountEntity createCreditAccountEntity(String userId, BigDecimal adjustAmount) {
        return CreditAccountEntity.builder().userId(userId).adjustAmount(adjustAmount).build();
    }

    public static CreditOrderEntity createCreditOrderEntity(String userId, TradeNameVO tradeNameVO,
                                                            TradeTypeVO tradeTypeVO, BigDecimal tradeAmount, String outBusinessNo) {
        return CreditOrderEntity.builder()
            .userId(userId)
            .orderId(RandomStringUtils.randomNumeric(12))
            .tradeNameVO(tradeNameVO)
            .tradeTypeVO(tradeTypeVO)
            .tradeAmount(tradeAmount)
            .outBusinessNo(outBusinessNo)
            .build();
    }

    public static TaskEntity createTaskEntity(String userId, String topic, String messageId, BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> message) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userId);
        taskEntity.setTopic(topic);
        taskEntity.setMessageId(messageId);
        taskEntity.setMessage(message);
        taskEntity.setState(TaskStateVO.create);
        return taskEntity;
    }
}
