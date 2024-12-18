package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 活动账单实体对象
 * @create 2024/11/1 18:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountEntity {

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 活动Id
     */
    private Long activityId;
    /**
     * 总次数
     */
    private Integer totalCount;
    /**
     * 总次数剩余
     */
    private Integer totalCountSurplus;
    /**
     * 日次数
     */
    private Integer dayCount;
    /**
     * 日次数剩余
     */
    private Integer dayCountSurplus;
    /**
     * 月次数
     */
    private Integer monthCount;
    /**
     * 月次数剩余
     */
    private Integer monthCountSurplus;
}
