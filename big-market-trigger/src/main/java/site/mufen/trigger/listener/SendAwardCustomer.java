package site.mufen.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.mufen.domain.award.event.SendAwardMessageEvent;
import site.mufen.domain.award.model.entity.DistributeAwardEntity;
import site.mufen.domain.award.service.AwardService;
import site.mufen.types.event.BaseEvent;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户奖品记录消息消费者
 * @create 2024-04-06 12:09
 */
@Slf4j
@Component
public class SendAwardCustomer {

    private final AwardService awardService;
    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    public SendAwardCustomer(AwardService awardService) {
        this.awardService = awardService;
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_award}"))
    public void listener(String message) {
        try {
            log.info("监听用户奖品发送消息 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage =
                JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
            }.getType());
            DistributeAwardEntity distributeAwardEntity = getDistributeAwardEntity(eventMessage);
            // 发放奖品
            awardService.distributeAward(distributeAwardEntity);

        } catch (Exception e) {
            log.error("监听用户奖品发送消息，消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }

    private static DistributeAwardEntity getDistributeAwardEntity(BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage) {
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = eventMessage.getData();

        DistributeAwardEntity distributeAwardEntity = new DistributeAwardEntity();
        distributeAwardEntity.setUserId(sendAwardMessage.getUserId());
        distributeAwardEntity.setOrderId(sendAwardMessage.getOrderId());
        distributeAwardEntity.setAwardId(sendAwardMessage.getAwardId());
        distributeAwardEntity.setAwardConfig(sendAwardMessage.getAwardConfig());
        return distributeAwardEntity;
    }

}
