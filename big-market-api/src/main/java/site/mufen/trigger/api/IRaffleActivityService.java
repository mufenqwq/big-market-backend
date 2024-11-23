package site.mufen.trigger.api;

import site.mufen.trigger.api.dto.*;
import site.mufen.types.model.Response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mufen
 * @Description 抽奖活动服务
 * @create 2024/11/6 00:50
 */
public interface IRaffleActivityService {

    /**
     * 活动装配 数据预热缓存
     * @param activityId 活动id
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @param request 请求对象
     * @return 返回抽奖结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 日历签到返利接口
     * @param userId 用户ID
     * @return 签到结果
     */
    Response<Boolean> calendarSignRebate(String userId);

    /**
     * 判断是否完成日历签到返利接口
     * @param userId 用户id
     * @return 查询结果
     */
    Response<Boolean> isCalendarSignRebate(String userId);

    /**
     * 查询用户活动接口
     * @param request 请求参数 [活动Id, 用户Id]
     * @return 返回结果 总额度 月额度 日额度
     */
    Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO request);

    /**
     * 查询当前活动的sku商品
     * @param activityId 活动Id
     * @return sku商品列表
     */
    Response<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId);
    /**
     * 查询用户积分额度
      * @return 返回积分额度
     */
    Response<BigDecimal> queryUserCreditAccount(String userId);
    /**
     * 积分兑换sku商品
     * @param request 请求参数 userId sku
     * @return 是否成功
     */
    Response<Boolean> creditPayExchangeSku(SkuProductShopCartRequestDTO request);
}
