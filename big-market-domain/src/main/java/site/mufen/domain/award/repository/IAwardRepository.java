package site.mufen.domain.award.repository;

import site.mufen.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * @author mufen
 * @Description 奖品仓储服务
 * @create 2024/11/5 17:27
 */
public interface IAwardRepository {

    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
}
