package site.mufen.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.mufen.domain.activity.model.entity.SkuRechargeEntity;
import site.mufen.domain.activity.service.IRaffleActivityAccountQuotaService;
import site.mufen.domain.rebate.event.SendRebateMessageEvent;
import site.mufen.domain.rebate.model.valobj.RebateTypeVO;
import site.mufen.types.event.BaseEvent;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 监听行为返利消息
 * @create 2024/11/9 21:47
 */
@Slf4j
@Component
public class RebateMessageCustomer {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户的行为返利消息 topic:{} message:{}", topic, message);
            // 1.转换消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());

            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();
            if (!RebateTypeVO.SKU.getCode().equals(rebateMessage.getRebateType())) {
                log.info("监听用户行为返利消息，非sku奖励暂时不处理 topic:{} message: {}", topic, message);
                return;
            }

            // 2. 入账奖励
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setSku(Long.parseLong(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            raffleActivityAccountQuotaService.createSkuRechargeOrder(skuRechargeEntity);

        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic: {} message: {}", topic, message);
        }
    }
}
