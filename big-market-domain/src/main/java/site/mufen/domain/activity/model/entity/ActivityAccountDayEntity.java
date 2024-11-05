package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mufen
 * @Description 活动账户 - 日参与记录表
 * @create 2024/11/4 10:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountDayEntity {

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 活动Id
     */
    private Long activityId;
    /**
     * 日期(yyyy-MM-dd)
     */
    private String day;
    /**
     * 日次数
     */
    private Integer dayCount;
    /**
     * 日次数剩余
     */
    private Integer dayCountSurplus;

}
