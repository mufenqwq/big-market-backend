package site.mufen.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description 用户积分奖品实体对象
 * @create 2024/11/13 14:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreditAwardEntity {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 积分奖品
     */
    private BigDecimal creditAmount;
}
