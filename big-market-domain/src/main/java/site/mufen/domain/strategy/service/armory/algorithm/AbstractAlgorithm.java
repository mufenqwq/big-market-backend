package site.mufen.domain.strategy.service.armory.algorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.mufen.domain.strategy.repository.IStrategyRepository;

import javax.annotation.Resource;
import java.security.SecureRandom;

/**
 * @author mufen
 * @Description
 * @create 2024/12/10 09:39
 */
public abstract class AbstractAlgorithm implements IAlgorithm {

    @Resource
    protected IStrategyRepository repository;

    protected final SecureRandom secureRandom = new SecureRandom();

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum Algorithm {
        O1("o1Algorithm"),
        OLogN("oLogNAlgorithm");
        private String key;
    }
}
