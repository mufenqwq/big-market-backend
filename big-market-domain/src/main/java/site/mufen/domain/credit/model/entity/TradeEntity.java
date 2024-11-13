package site.mufen.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.credit.model.valobj.TradeNameVO;
import site.mufen.domain.credit.model.valobj.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 00:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeEntity {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 交易名称
     */
    private TradeNameVO tradeNameVO;
    /**
     * 交易类型 forward-正向 reverse-反向
     */
    private TradeTypeVO tradeTypeVO;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 外部透穿业务Id 保证幂等性
     */
    private String outBusinessNo;
}
