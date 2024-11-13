package site.mufen.trigger.api.dto;

import lombok.Data;

/**
 * @author mufen
 * @Description
 * @create 2024/11/10 21:33
 */
@Data
public class UserActivityAccountRequestDTO {
    private String userId;
    private Long activityId;
}
