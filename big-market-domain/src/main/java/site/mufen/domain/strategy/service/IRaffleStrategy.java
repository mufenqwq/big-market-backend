package site.mufen.domain.strategy.service;

import site.mufen.domain.strategy.model.entity.RaffleAwardEntity;
import site.mufen.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author mufen
 * @Description 抽奖策略接口
 * @create 2024/10/17 20:24
 */
public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
