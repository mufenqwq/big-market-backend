package site.mufen.test.domain.credit;

import site.mufen.domain.credit.model.entity.TradeEntity;
import site.mufen.domain.credit.model.valobj.TradeNameVO;
import site.mufen.domain.credit.model.valobj.TradeTypeVO;
import site.mufen.domain.credit.service.ICreditAdjustService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 积分额度增加服务测试
 * @create 2024-06-01 10:22
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditAdjustServiceTest {

    @Resource
    private ICreditAdjustService creditAdjustService;

    @Test
    public void test_createOrder_forward() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeNameVO(TradeNameVO.REBATE);
        tradeEntity.setTradeTypeVO(TradeTypeVO.FORWARD);
        tradeEntity.setTradeAmount(new BigDecimal("10.19"));
        tradeEntity.setOutBusinessNo("100009909911");
        creditAdjustService.createOrder(tradeEntity);
    }

    @Test
    public void test_createOrder_reverse() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeNameVO(TradeNameVO.REBATE);
        tradeEntity.setTradeTypeVO(TradeTypeVO.REVERSE);
        tradeEntity.setTradeAmount(new BigDecimal("-10.19"));
        tradeEntity.setOutBusinessNo("20000990991");
        creditAdjustService.createOrder(tradeEntity);
    }

    @Test
    public void test_createOrder_pay() throws InterruptedException {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeNameVO(TradeNameVO.CONVERT_SKU);
        tradeEntity.setTradeTypeVO(TradeTypeVO.REVERSE);
        tradeEntity.setTradeAmount(new BigDecimal("-1.68"));
        tradeEntity.setOutBusinessNo("70009240609005");
        creditAdjustService.createOrder(tradeEntity);

        new CountDownLatch(1).await();
    }


}
