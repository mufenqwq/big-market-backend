package site.mufen.trigger.listener;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.mufen.domain.activity.model.entity.DeliveryOrderEntity;
import site.mufen.domain.activity.service.IRaffleActivityAccountQuotaService;
import site.mufen.domain.credit.event.CreditAdjustSuccessMessageEvent;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.event.BaseEvent;
import site.mufen.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 积分调整消息消费者
 * @create 2024/11/15 01:02
 */
@Slf4j
@Component
public class CreditAdjustSuccessConsumer {

    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;

   @Resource
   private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

   @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.credit_adjust_success}"))
   public void listener(String message) {
      try {
         log.info("监听积分账户调整成功消息，进行交易商品发货 topic:{} message:{}", topic, message);

          BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>>() {
          }.getType());
          CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = eventMessage.getData();
          // 积分发货
          DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
          deliveryOrderEntity.setUserId(creditAdjustSuccessMessage.getUserId());
          deliveryOrderEntity.setOutBusinessNo(creditAdjustSuccessMessage.getOutBusinessNo());
         raffleActivityAccountQuotaService.updateOrder(deliveryOrderEntity);
      } catch (AppException e) {
          if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
              log.warn("监听积分账户调整成功消息，进行交易商品发货，消费重复 topic:{} message:{}", topic, message, e);
              return;
          }
          throw e;
      } catch (Exception e) {
          log.error("监听积分账户调整成功消息，进行交易商品发货失败 topic: {} message: {}", topic, message, e);
          throw e;
      }
   }
}
