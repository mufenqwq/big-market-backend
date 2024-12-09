package site.mufen.infrastructure.dao.po;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 抽奖活动账户表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleActivityAccount {

  /**
   * 自增Id
   */
  private Long id;
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
  private Date createTime;
  private Date updateTime;

}
