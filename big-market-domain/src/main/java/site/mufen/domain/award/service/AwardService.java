package site.mufen.domain.award.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.mufen.domain.award.event.SendAwardMessageEvent;
import site.mufen.domain.award.model.aggregate.UserAwardRecordAggregate;
import site.mufen.domain.award.model.entity.DistributeAwardEntity;
import site.mufen.domain.award.model.entity.TaskEntity;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;
import site.mufen.domain.award.model.valobj.TaskStateVO;
import site.mufen.domain.award.repository.IAwardRepository;
import site.mufen.domain.award.service.distribute.IDistributeAward;
import site.mufen.types.event.BaseEvent;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author mufen
 * @Description 奖品服务
 * @create 2024/11/5 17:30
 */
@Service
@Slf4j
public class AwardService implements IAwardService {


    private final IAwardRepository awardRepository;


    private final SendAwardMessageEvent sendAwardMessageEvent;

    private final Map<String, IDistributeAward> distributeAwardMap;

    public AwardService(IAwardRepository awardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDistributeAward> distributeAwardMap) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        // 构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);
        
        // 构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVO.create);

        // 构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = new UserAwardRecordAggregate();
        userAwardRecordAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        userAwardRecordAggregate.setTaskEntity(taskEntity);

        // 存储聚合对象 一个事务下面 用户的中奖记录
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);


    }

    /**
     * 分发奖品
     * @param distributeAwardEntity 风阀奖品对象
     */
    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        // 奖品key
        String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        if (null == awardKey) {
            log.error("分发奖品，奖品Key不存在。awardId:{}", distributeAwardEntity.getAwardId());
            return;
        }

        // 奖品服务
        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
        if (null == distributeAward) {
            log.error("分发奖品，对应服务不存在。awardKey:{}", awardKey);
            return;
        }

        // 分发奖品
        distributeAward.giveOutPrizes(distributeAwardEntity);

    }
}
