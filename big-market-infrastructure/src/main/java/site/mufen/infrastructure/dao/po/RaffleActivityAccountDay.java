package site.mufen.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
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
public class RaffleActivityAccountDay {

    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 自增Id
     */
    private String id;
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
    private Date createTime;
    private Date updateTime;

    public String currentDay() {
        return dateFormatDay.format(new Date());
    }
}
