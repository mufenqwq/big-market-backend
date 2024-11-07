package site.mufen.trigger.api;

import site.mufen.trigger.api.dto.ActivityDrawRequestDTO;
import site.mufen.trigger.api.dto.ActivityDrawResponseDTO;
import site.mufen.types.model.Response;
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
}
