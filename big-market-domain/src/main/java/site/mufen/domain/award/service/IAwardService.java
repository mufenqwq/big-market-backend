package site.mufen.domain.award.service;

import site.mufen.domain.award.model.entity.DistributeAwardEntity;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author mufen
 * @Description 奖品服务接口
 * @create 2024/11/5 16:56
 */
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    void distributeAward(DistributeAwardEntity distributeAwardEntity);
}
