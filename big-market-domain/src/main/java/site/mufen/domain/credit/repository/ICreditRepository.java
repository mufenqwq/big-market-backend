package site.mufen.domain.credit.repository;

import site.mufen.domain.credit.model.aggregate.TradeAggregate;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 00:00
 */
public interface ICreditRepository {
    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);
}
