package site.mufen.domain.strategy.service.rule.chain;

/**
 * @author mufen
 * @Description 装配接口
 * @create 2024/10/21 12:46
 */
public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);
}
