package site.mufen.domain.award.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.mufen.types.event.BaseEvent;

import java.util.Date;

/**
 * @author mufen
 * @Description
 * @create 2024/11/5 17:06
 */
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {
    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;


    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
        return EventMessage.<SendAwardMessage>builder()
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
    public static class SendAwardMessage {
        /**
         * 用户Id
         */
        private String userId;
        /**
         * 订单Id
         */
        private String orderId;
        /**
         * 奖品Id
         */
        private Integer awardId;
        /**
         * 奖品名称
         */
        private String awardTitle;
        /**
         * 奖品配置信息
         */
        private String awardConfig;
    }
}
