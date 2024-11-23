package site.mufen.domain.credit.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.mufen.types.event.BaseEvent;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 23:08
 */
@Service
public class CreditAdjustSuccessMessageEvent extends BaseEvent<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> {

    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;



    @Override
    public EventMessage<CreditAdjustSuccessMessage> buildEventMessage(CreditAdjustSuccessMessage data) {
        return EventMessage.<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>builder()
            .id(RandomStringUtils.randomNumeric(11))
            .timestamp(new Date())
            .data(data).build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreditAdjustSuccessMessage {
        private String userId;
        private String orderId;
        private BigDecimal amount;
        private String outBusinessNo;
    }
}
