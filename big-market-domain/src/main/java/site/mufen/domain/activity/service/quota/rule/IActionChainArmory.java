package site.mufen.domain.activity.service.quota.rule;

/**
 * @author mufen
 * @Description 抽奖动作责任链装配
 * @create 2024/11/2 18:27
 */
public interface IActionChainArmory {
    IActionChain next();

    IActionChain appendNext(IActionChain next);
}
