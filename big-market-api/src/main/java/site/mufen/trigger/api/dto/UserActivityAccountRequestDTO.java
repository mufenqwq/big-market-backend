package site.mufen.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mufen
 * @Description
 * @create 2024/11/10 21:33
 */
@Data
public class UserActivityAccountRequestDTO implements Serializable {
    private String userId;
    private Long activityId;
}
