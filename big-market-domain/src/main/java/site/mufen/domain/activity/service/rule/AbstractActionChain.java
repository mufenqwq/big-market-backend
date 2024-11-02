package site.mufen.domain.activity.service.rule;

/**
 * @author mufen
 * @Description 下单规则责任链抽象类
 * @create 2024/11/2 18:30
 */
public abstract class AbstractActionChain implements IActionChain{

    private IActionChain next;

    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }
}
