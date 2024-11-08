package site.mufen.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.mufen.domain.activity.model.entity.UserRaffleOrderEntity;
import site.mufen.domain.activity.service.IRaffleActivityPartakeService;
import site.mufen.domain.activity.service.armory.IActivityArmory;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;
import site.mufen.domain.award.model.valobj.AwardStateVO;
import site.mufen.domain.award.service.IAwardService;
import site.mufen.domain.strategy.model.entity.RaffleAwardEntity;
import site.mufen.domain.strategy.model.entity.RaffleFactorEntity;
import site.mufen.domain.strategy.service.IRaffleStrategy;
import site.mufen.domain.strategy.service.armory.IStrategyArmory;
import site.mufen.trigger.api.IRaffleActivityService;
import site.mufen.trigger.api.dto.*;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;
import site.mufen.types.model.Response;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mufen
 * @Description
 * @create 2024/11/6 11:28
 */
@Slf4j
@RestController()
@CrossOrigin(origins = "*")
@RequestMapping("/api/${app.config.api-version}/raffle/activity")
public class RaffleActivityController implements IRaffleActivityService {

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
}

