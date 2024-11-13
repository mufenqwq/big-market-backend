package site.mufen.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 00:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditAccountEntity {
    private String userId;
    /**
     * 可用积分，每次扣减的值
     */
    private BigDecimal adjustAmount;
}
