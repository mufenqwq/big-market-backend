package site.mufen.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;
import site.mufen.domain.award.model.entity.UserCreditAwardEntity;
import site.mufen.domain.award.model.valobj.AwardStateVO;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description 发送奖品聚合对象
 * @create 2024/11/13 14:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiveOutPrizesAggregate {
    private String userId;
    /**
     * 用户中奖记录
     */
    private UserAwardRecordEntity userAwardRecordEntity;
    /**
     * 用户积分奖品
     */
    private UserCreditAwardEntity userCreditAwardEntity;

    public static UserAwardRecordEntity buildDistributeUserAwardRecord(String userId, String orderId, Integer awardId, AwardStateVO awardStateVO) {
        UserAwardRecordEntity userAwardRecord = new UserAwardRecordEntity();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setOrderId(orderId);
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setAwardState(awardStateVO);
        return userAwardRecord;
    }

    public static UserCreditAwardEntity buildUserCreditAwardEntity(String userId, BigDecimal creditAmount) {
        return UserCreditAwardEntity.builder().userId(userId).creditAmount(creditAmount).build();

    }


}
