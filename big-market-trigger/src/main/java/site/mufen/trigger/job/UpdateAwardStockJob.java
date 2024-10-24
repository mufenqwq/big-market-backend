package site.mufen.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.mufen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import site.mufen.domain.strategy.service.IRaffleStock;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 更新奖品库存任务 为了不让更新库存的压力给到数据库 这里使用redis更新缓存库存 异步队列更新数据库 数据库表最终一致即可
 * @create 2024/10/24 01:09
 */
@Slf4j
@Component()
public class UpdateAwardStockJob {

    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        log.info("定时任务，更新奖品库存 【延迟队列获取， 降低对数据库的更新频次，不要产生竞争】");
        StrategyAwardStockKeyVO strategyAwardStockKeyVO = null;
        try {
            strategyAwardStockKeyVO = raffleStock.takeQueueValue();
            if (null == strategyAwardStockKeyVO) return;
            log.info("定时任务，更新奖品库存 strategyId:{} awardId:{}", strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
            raffleStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
        } catch (InterruptedException e) {
            log.info("定时任务， 更新奖品库存失败", e);
        }


    }
}
