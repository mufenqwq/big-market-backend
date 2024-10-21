package site.mufen.domain.strategy.service.rule.chain;

/**
 * @author mufen
 * @Description
 * @create 2024/10/21 11:15
 */
public abstract class AbstractLogicChain implements ILogicChain{

    private ILogicChain next;

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    protected abstract String ruleModel();
}
