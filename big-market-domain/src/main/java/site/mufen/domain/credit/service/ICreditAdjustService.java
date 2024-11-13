package site.mufen.domain.credit.service;

import site.mufen.domain.credit.model.entity.TradeEntity;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 00:07
 */
public interface ICreditAdjustService {
    String createOrder(TradeEntity tradeEntity);
}
