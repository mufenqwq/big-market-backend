package site.mufen.domain.award.service.distribute.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import site.mufen.domain.award.model.aggregate.GiveOutPrizesAggregate;
import site.mufen.domain.award.model.entity.DistributeAwardEntity;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;
import site.mufen.domain.award.model.entity.UserCreditAwardEntity;
import site.mufen.domain.award.model.valobj.AwardStateVO;
import site.mufen.domain.award.repository.IAwardRepository;
import site.mufen.domain.award.service.distribute.IDistributeAward;
import site.mufen.types.common.Constants;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author mufen
 * @Description 用户随机积分发放
 * @create 2024/11/13 12:23
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Resource
    private IAwardRepository awardRepository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        Integer awardId = distributeAwardEntity.getAwardId();
        String awardConfig = distributeAwardEntity.getAwardConfig();

        if (StringUtils.isBlank(awardConfig)) { // 1 100
            awardConfig = awardRepository.queryAwardConfig(awardId);
        }
        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length != 2) {
            throw new RuntimeException("awardConfig不是一个范围值，比如1,100");
        }
        // 生成随机积分值
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));

        // 构建聚合对象
        UserAwardRecordEntity userAwardRecord = GiveOutPrizesAggregate.buildDistributeUserAwardRecord(
            distributeAwardEntity.getUserId(),
            distributeAwardEntity.getOrderId(),
            distributeAwardEntity.getAwardId(),
            AwardStateVO.completed
        );
        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildUserCreditAwardEntity(distributeAwardEntity.getUserId(), creditAmount);
        GiveOutPrizesAggregate giveOutPrizesAggregate = GiveOutPrizesAggregate.builder()
            .userId(distributeAwardEntity.getUserId())
            .userAwardRecordEntity(userAwardRecord)
            .userCreditAwardEntity(userCreditAwardEntity)
            .build();

        // 存储发奖对象
        awardRepository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);
    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) return min;
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
