package site.mufen.trigger.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.*;
import site.mufen.domain.activity.model.entity.*;
import site.mufen.domain.activity.model.valobj.OrderTradeTypeVO;
import site.mufen.domain.activity.service.IRaffleActivityAccountQuotaService;
import site.mufen.domain.activity.service.IRaffleActivityPartakeService;
import site.mufen.domain.activity.service.IRaffleActivitySkuProductService;
import site.mufen.domain.activity.service.armory.IActivityArmory;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;
import site.mufen.domain.award.model.valobj.AwardStateVO;
import site.mufen.domain.award.service.IAwardService;
import site.mufen.domain.credit.model.entity.CreditAccountEntity;
import site.mufen.domain.credit.model.entity.TradeEntity;
import site.mufen.domain.credit.model.valobj.TradeNameVO;
import site.mufen.domain.credit.model.valobj.TradeTypeVO;
import site.mufen.domain.credit.service.ICreditAdjustService;
import site.mufen.domain.rebate.model.entity.BehaviorEntity;
import site.mufen.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import site.mufen.domain.rebate.model.valobj.BehaviorTypeVO;
import site.mufen.domain.rebate.service.IBehaviorRebateService;
import site.mufen.domain.strategy.model.entity.RaffleAwardEntity;
import site.mufen.domain.strategy.model.entity.RaffleFactorEntity;
import site.mufen.domain.strategy.service.IRaffleStrategy;
import site.mufen.domain.strategy.service.armory.IStrategyArmory;
import site.mufen.trigger.api.IRaffleActivityService;
import site.mufen.trigger.api.dto.*;
import site.mufen.types.annotations.DCCValue;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;
import site.mufen.types.model.Response;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mufen
 * @Description
 * @create 2024/11/6 11:28
 */
@Slf4j
@RestController()
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/${app.config.api-version}/raffle/activity")
@DubboService(version = "1.0")
public class RaffleActivityController implements IRaffleActivityService {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private IAwardService awardService;
    @Resource
    private IActivityArmory activityArmory;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IBehaviorRebateService behaviorRebateService;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @Resource
    private IRaffleActivitySkuProductService raffleActivitySkuProductService;
    @Resource
    private ICreditAdjustService creditAdjustService;

    @DCCValue("degradeSwitch:open")
    private String degradeSwitch;

    /**
     * 活动装配 - 数据预热 | 把活动配置的对应的 sku 一起装配
     *
     * @param activityId 活动ID
     * @return 装配结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/armory">/api/v1/raffle/activity/armory</a>
     * 入参：{"activityId":100001,"userId":"xiaofuge"}
     * <p>
     * curl --request GET \
     * --url 'http://localhost:8091/api/v1/raffle/activity/armory?activityId=100301'
     */
    @GetMapping("armory")
    @Override
    public Response<Boolean> armory(@RequestParam Long activityId) {
        try {
            log.info("活动装配，数据预热，开始: activityId:{}", activityId);
            if ("open".equals(degradeSwitch)) {
                return Response.<Boolean>builder()
                    .code(ResponseCode.DEGRADE_SWITCH.getCode())
                    .info(ResponseCode.DEGRADE_SWITCH.getInfo())
                    .data(true)
                    .build();
            }
            // 1.活动装配
            activityArmory.assembleActivitySkuByActivityId(activityId);
            // 2. 策略装配
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);
            return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(true)
                .build();
        } catch (Exception e) {
            log.error("活动装配，数据预热，失败 activityId:{}", activityId, e);
            return Response.<Boolean>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .build();
        }
    }


    /**
     * 抽奖接口
     *
     * @param request 请求对象
     * @return 抽奖结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/draw">/api/v1/raffle/activity/draw</a>
     * 入参：{"activityId":100001,"userId":"xiaofuge"}
     * <p>
     * curl --request POST \
     * --url http://localhost:8091/api/v1/raffle/activity/draw \
     * --header 'content-type: application/json' \
     * --data '{
     * "userId":"xiaofuge",
     * "activityId": 100301
     * }'
     */
    @Override
    @PostMapping("draw")
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("活动抽奖 开始 userId:{}, activityId:{}", request.getUserId(), request.getActivityId());
            // 如果活动已经降级了，直接返回结果
            // 降级开关 open 开启  close 关闭
            if (StringUtils.isNotBlank(degradeSwitch) && "open".equals(degradeSwitch)) {
                return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.DEGRADE_SWITCH.getCode())
                    .info(ResponseCode.DEGRADE_SWITCH.getInfo())
                    .build();
            }
            // 1.参数校验
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            //2.参与活动 - 创建参与记录订单
            UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(request.getUserId(), request.getActivityId());
            //3.抽奖策略 - 执行抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                .userId(userRaffleOrder.getUserId())
                .strategyId(userRaffleOrder.getStrategyId())
                .endDateTime(userRaffleOrder.getEndDateTime())
                .build());
            //4. 存放结果 - 写入抽奖结果
            UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder()
                .userId(userRaffleOrder.getUserId())
                .activityId(userRaffleOrder.getActivityId())
                .strategyId(userRaffleOrder.getStrategyId())
                .orderId(userRaffleOrder.getOrderId())
                .awardId(raffleAwardEntity.getAwardId())
                .awardTitle(raffleAwardEntity.getAwardTitle())
                .awardTime(new Date())
                .awardState(AwardStateVO.create)
                .awardConfig(raffleAwardEntity.getAwardConfig())
                .build();
            awardService.saveUserAwardRecord(userAwardRecord);

            //5. 返回结果
            return Response.<ActivityDrawResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(ActivityDrawResponseDTO.builder()
                    .awardId(userAwardRecord.getAwardId())
                    .awardTitle(userAwardRecord.getAwardTitle())
                    .awardIndex(raffleAwardEntity.getSort()).build()
                ).build();
        } catch (AppException e) {
            log.error("活动抽奖失败 userId:{}, activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                .code(e.getCode())
                .info(e.getInfo())
                .build();
        } catch (Exception e) {
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .build();
        }
    }

    /**
     * 日历签到返利接口
     *
     * @param userId 用户ID
     * @return 签到返利结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/calendar_sign_rebate">/api/v1/raffle/activity/calendar_sign_rebate</a>
     * 入参：xiaofuge
     * <p>
     * curl -X POST http://localhost:8091/api/v1/raffle/activity/calendar_sign_rebate -d "userId=xiaofuge" -H "Content-Type: application/x-www-form-urlencoded"
     */
    @PostMapping("calendar_sign_rebate")
    @Override
    public Response<Boolean> calendarSignRebate(@RequestParam String userId) {
        try {
            log.info("日历签到返利开始 userId:{}", userId);
            BehaviorEntity behaviorEntity = BehaviorEntity.builder()
                .userId(userId)
                .behaviorTypeVO(BehaviorTypeVO.SIGN)
                .outBusinessNo(simpleDateFormat.format(new Date())).build();
            List<String> orderIds = behaviorRebateService.createOrder(behaviorEntity);
            log.info("日历签到返利完成 userId:{} orderIds:{}", userId, orderIds);
            return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(true)
                .build();
        } catch (AppException e) {
            log.error("日历签到返利异常 userId:{}", userId);
            return Response.<Boolean>builder()
                .code(e.getCode())
                .info(e.getInfo())
                .data(false)
                .build();
        } catch (Exception e) {
            log.error("日历签到返利失败 userId:{}", userId);
            return Response.<Boolean>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .data(false)
                .build();
        }
    }

    @PostMapping("is_calendar_sign_rebate")
    @Override
    public Response<Boolean> isCalendarSignRebate(@RequestParam String userId) {
        try {
            log.info("查询用户是否完成日历签到返利 【start】 userId:{}", userId);
            String outBusinessNo = simpleDateFormat.format(new Date());
            List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = behaviorRebateService.queryOrderByOutBusinessNo(userId, outBusinessNo);
            log.info("查询用户是否完成日历签到返利 【completed】 userId:{} orders.size:{}", userId, behaviorRebateOrderEntities.size());
            return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(!behaviorRebateOrderEntities.isEmpty())
                .build();
        } catch (Exception e) {
            log.error("查询用户是否完成日历签到返利 failed 【userId】:{}", userId);
            return Response.<Boolean>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .data(false)
                .build();
        }
    }

    @PostMapping("query_user_activity_account")
    @Override
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO request) {
        try {
            log.info("查询用户活动账户 [start] userId:{} activityId:{}", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuotaService.queryUserActivityAccount(request.getActivityId(), request.getUserId());
            UserActivityAccountResponseDTO userActivityAccountResponseDTO = new UserActivityAccountResponseDTO();
            userActivityAccountResponseDTO.setTotalCount(activityAccountEntity.getTotalCount());
            userActivityAccountResponseDTO.setTotalCountSurplus(activityAccountEntity.getTotalCountSurplus());
            userActivityAccountResponseDTO.setMonthCount(activityAccountEntity.getMonthCount());
            userActivityAccountResponseDTO.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
            userActivityAccountResponseDTO.setDayCount(activityAccountEntity.getDayCount());
            userActivityAccountResponseDTO.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());

            log.info("查询用户活动账户开始 [completed] userId:{} activityId:{} dto:{}", request.getUserId(), request.getActivityId(), userActivityAccountResponseDTO);
            return Response.<UserActivityAccountResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(userActivityAccountResponseDTO).build();
        } catch (Exception e) {
            log.info("查询用户活动账户 [failed] userId:{} activityId:{}", request.getUserId(), request.getActivityId());
            return Response.<UserActivityAccountResponseDTO>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .build();
        }
    }
    @PostMapping("query_sku_product_list_by_activity_id")
    @Override
    public Response<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId) {
        try {
            log.info("查询sku商品集合开始，activityId:{}", activityId);
            // 1. 参数校验
            if (null == activityId) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            List<SkuProductEntity> skuProductEntities = raffleActivitySkuProductService.querySkuProductEntityListByActivityId(activityId);
            ArrayList<SkuProductResponseDTO> skuProductResponseDTOS = new ArrayList<>(skuProductEntities.size());
            for (SkuProductEntity skuProductEntity : skuProductEntities) {

                SkuProductResponseDTO.ActivityCount activityCount = new SkuProductResponseDTO.ActivityCount();
                activityCount.setTotalCount(skuProductEntity.getActivityCount().getTotalCount());
                activityCount.setMonthCount(skuProductEntity.getActivityCount().getMonthCount());
                activityCount.setDayCount(skuProductEntity.getActivityCount().getDayCount());

                SkuProductResponseDTO skuProductResponseDTO = new SkuProductResponseDTO();
                skuProductResponseDTO.setSku(skuProductEntity.getSku());
                skuProductResponseDTO.setActivityId(skuProductEntity.getActivityId());
                skuProductResponseDTO.setActivityCountId(skuProductEntity.getActivityCountId());
                skuProductResponseDTO.setStockCount(skuProductEntity.getStockCount());
                skuProductResponseDTO.setStockCountSurplus(skuProductEntity.getStockCountSurplus());
                skuProductResponseDTO.setProductAmount(skuProductEntity.getProductAmount());
                skuProductResponseDTO.setActivityCount(activityCount);

                skuProductResponseDTOS.add(skuProductResponseDTO);

            }
            log.info("查询sku商品集合完成 activityId:{} skuProductResponseDTOS:{}", activityId, JSON.toJSONString(skuProductResponseDTOS));
            return Response.<List<SkuProductResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(skuProductResponseDTOS)
                .build();
        } catch (Exception e) {
            log.error("查询sku商品集合失败 activityId:{}", activityId, e);
            return Response.<List<SkuProductResponseDTO>>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .build();
        }
    }
    @PostMapping("query_user_credit_account")
    @Override
    public Response<BigDecimal> queryUserCreditAccount(String userId) {
        try {
            log.info("查询用户积分值开始 userId:{}", userId);
            CreditAccountEntity creditAccountEntity= creditAdjustService.queryUserCreditAccount(userId);

            return Response.<BigDecimal>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(creditAccountEntity.getAdjustAmount())
                .build();
        } catch (Exception e) {
            log.error("查询用户积分值失败, userId:{}", userId);
            return Response.<BigDecimal>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .build();
        }
    }
    @PostMapping("credit_pay_exchange_sku")
    @Override
    public Response<Boolean> creditPayExchangeSku(@RequestBody SkuProductShopCartRequestDTO request) {
        try {
            log.info("积分兑换商品开始 userId:{} sku:{} ", request.getUserId(), request.getSku());
            UnpaidActivityOrderEntity skuRechargeOrder = raffleActivityAccountQuotaService.createSkuRechargeOrder(SkuRechargeEntity.builder()
                .userId(request.getUserId())
                .sku(request.getSku())
                .outBusinessNo(RandomStringUtils.randomNumeric(12))
                .orderTradeTypeVO(OrderTradeTypeVO.credit_pay_trade)
                .build());
            log.info("积分兑换，创建未支付订单完成 userId:{}, sku:{}, outBusinessNo:{}", request.getUserId(), request.getSku(), skuRechargeOrder.getOutBusinessNo());
            String orderId = creditAdjustService.createOrder(TradeEntity.builder()
                .userId(skuRechargeOrder.getUserId())
                .tradeNameVO(TradeNameVO.CONVERT_SKU)
                .tradeTypeVO(TradeTypeVO.REVERSE)
                .tradeAmount(skuRechargeOrder.getPayAmount().negate())
                .outBusinessNo(skuRechargeOrder.getOutBusinessNo())
                .build());
            log.info("积分兑换，支付订单完成 userId:{}, sku:{}, outBusinessNo:{} orderId:{}", request.getUserId(), request.getSku(), skuRechargeOrder.getOutBusinessNo(), orderId);

            return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(true)
                .build();
        } catch (Exception e) {
            log.error("积分兑换，支付订单错误 userId:{}, sku:{}", request.getUserId(), request.getSku());
            return Response.<Boolean>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .data(false)
                .build();
        }
    }
}

