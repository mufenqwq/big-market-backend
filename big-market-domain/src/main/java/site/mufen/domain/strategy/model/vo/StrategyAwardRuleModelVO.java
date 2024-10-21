package site.mufen.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.mufen.domain.strategy.service.rule.factory.DefaultLogicFactory;
import site.mufen.types.common.Constants;

import java.util.ArrayList;

/**
 * @author mufen
 * @Description 抽奖策略规则值对象 没有唯一ID 仅限于从数据库查询对象
 * @create 2024/10/20 15:32
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

    public String[] raffleCenterRuleModelList() {
        if (null == ruleModels) {
            return null;
        }
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        ArrayList<String> ruleModelList = new ArrayList<>();
        for (String ruleModelValue : ruleModelValues) {
            if (DefaultLogicFactory.isCenter(ruleModelValue)) {
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }
}
