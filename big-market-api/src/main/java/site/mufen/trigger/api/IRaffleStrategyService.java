package site.mufen.trigger.api;


import site.mufen.trigger.api.dto.*;
import site.mufen.types.model.Response;

import java.util.List;

/**
 * @author mufen
 * @Description 抽奖服务接口
 * @create 2024/10/24 23:58
 */
public interface IRaffleStrategyService {
    /**
     * 策略装配接口
     * @param strategyId 策略Id
     * @return 是否成功
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询奖品列表接口
     * @param requestDTO 奖品奖品列表查询请求对象
     * @return 奖品列表数据
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     *  随机抽奖接口
     * @param requestDTO 请求参数
     * @return 抽奖结果
     */
    Response<RaffleStrategyResponseDTO> randomRaffle(RaffleStrategyRequestDTO requestDTO);

    Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(RaffleStrategyRuleWeightRequestDTO request);
}
