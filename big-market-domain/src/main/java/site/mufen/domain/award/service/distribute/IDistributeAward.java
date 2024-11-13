package site.mufen.domain.award.service.distribute;

import site.mufen.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author mufen
 * @Description 分发奖品接口
 * @create 2024/11/13 12:20
 */
public interface IDistributeAward {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);
}
