package site.mufen.domain.rebate.event;

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
 * @Description 发送返利消息事件
 * @create 2024/11/8 22:56
 */
@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Override
    public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
        return EventMessage.<RebateMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RebateMessage {
        /**
         * 用户Id
         */
        private String userId;
        /**
         * 返利描述
         */
        private String rebateDesc;
        /**
         * 返利类型 sku-活动库存充值实体 integral-随机积分
         */
        private String rebateType;
        /**
         * 返利配置 sku integral-积分值
         */
        private String rebateConfig;
        /**
         * 唯一业务id实现幂等
         */
        private String bizId;
    }
}
