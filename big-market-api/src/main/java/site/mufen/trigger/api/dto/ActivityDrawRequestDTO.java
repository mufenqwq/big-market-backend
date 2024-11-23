package site.mufen.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mufen
 * @Description 活动抽奖请求参数
 * @create 2024/11/6 11:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDrawRequestDTO implements Serializable {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 活动Id
     */
    private Long activityId;
}
