package site.mufen.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mufen
 * @Description
 * @create 2024/11/6 11:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDrawResponseDTO implements Serializable {
    // 奖品Id
    private Integer awardId;
    // 奖品标题
    private String  awardTitle;
    // 排序索引 用于前端展示
    private Integer awardIndex;
}
