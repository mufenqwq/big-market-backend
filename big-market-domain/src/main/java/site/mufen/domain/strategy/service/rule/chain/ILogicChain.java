package site.mufen.domain.strategy.service.rule.chain;

/**
 * @author mufen
 * @Description 责任链接口
 * @create 2024/10/21 11:12
 */
public interface ILogicChain extends ILogicChainArmory {

    /**
     * 责任链接口
     * @param userId 用户Id
     * @param strategyId 策略Id
     * @return 奖品Id
     */
    Integer logic(String userId, Long strategyId);


}
