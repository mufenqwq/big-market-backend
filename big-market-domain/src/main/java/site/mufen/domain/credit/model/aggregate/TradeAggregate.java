package site.mufen.domain.credit.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import site.mufen.domain.credit.model.entity.CreditAccountEntity;
import site.mufen.domain.credit.model.entity.CreditOrderEntity;
import site.mufen.domain.credit.model.valobj.TradeNameVO;
import site.mufen.domain.credit.model.valobj.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description 交易积分聚合对象
 * @create 2024/11/14 00:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeAggregate {
    private String userId;
    private CreditAccountEntity creditAccountEntity;
    private CreditOrderEntity creditOrderEntity;

    public static CreditAccountEntity createCreditAccountEntity(String userId, BigDecimal adjustAmount) {
        return CreditAccountEntity.builder().userId(userId).adjustAmount(adjustAmount).build();
    }

    public static CreditOrderEntity createCreditOrderEntity(String userId, TradeNameVO tradeNameVO,
                                                            TradeTypeVO tradeTypeVO, BigDecimal tradeAmount, String outBusinessNo) {
        return CreditOrderEntity.builder()
            .userId(userId)
            .orderId(RandomStringUtils.randomNumeric(12))
            .tradeNameVO(tradeNameVO)
            .tradeTypeVO(tradeTypeVO)
            .tradeAmount(tradeAmount)
            .outBusinessNo(outBusinessNo)
            .build();
    }
}
