package site.mufen.domain.rebate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import site.mufen.domain.rebate.model.entity.TaskEntity;
import site.mufen.domain.rebate.model.valobj.TaskStateVO;
import site.mufen.domain.rebate.event.SendRebateMessageEvent;
import site.mufen.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import site.mufen.domain.rebate.model.entity.BehaviorEntity;
import site.mufen.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import site.mufen.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import site.mufen.domain.rebate.repository.IBehaviorRebateRepository;
import site.mufen.types.common.Constants;
import site.mufen.types.event.BaseEvent;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mufen
 * @Description 行为返利服务实现
 * @create 2024/11/8 22:37
 */
@Service
@Slf4j
public class BehaviorRebateService implements IBehaviorRebateService {

    @Resource
    private final SendRebateMessageEvent sendRebateMessageEvent;
    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;

    public BehaviorRebateService(SendRebateMessageEvent sendRebateMessageEvent) {
        this.sendRebateMessageEvent = sendRebateMessageEvent;
    }

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        // 1. 查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorTypeVO());
        if (null == dailyBehaviorRebateVOS || dailyBehaviorRebateVOS.isEmpty()) return Collections.emptyList();
        // 2. 构建聚合对象
        ArrayList<String> orderIds = new ArrayList<>();
        ArrayList<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
            // 拼装业务Id: 用户ID_返利类型_外部透穿业务Id
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .bizId(bizId)
                    .build();
            orderIds.add(behaviorRebateOrderEntity.getOrderId());

            // MQ消息
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .bizId(bizId)
                    .build();

            // 构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            // 组装任务对象
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(behaviorEntity.getUserId());
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessageId(rebateMessageEventMessage.getId());
            taskEntity.setMessage(rebateMessageEventMessage);
            taskEntity.setState(TaskStateVO.create);

            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .userId(behaviorEntity.getUserId())
                    .taskEntity(taskEntity)
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .build();

            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }

        // 3.存储聚合对象数据
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregates);
        //4. 返回订单id集合
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByOutBusinessNo(userId, outBusinessNo);
    }
}
