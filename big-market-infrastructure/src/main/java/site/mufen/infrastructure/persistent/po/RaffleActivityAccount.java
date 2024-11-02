package site.mufen.infrastructure.persistent.po;


import lombok.Data;
import java.util.Date;

/**
 * 抽奖活动账户表
 */
@Data
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
  private Long totalCount;
  /**
   * 总次数剩余
   */
  private Long totalCountSurplus;
  /**
   * 日次数
   */
  private Long dayCount;
  /**
   * 日次数剩余
   */
  private Long dayCountSurplus;
  /**
   * 月次数
   */
  private Long monthCount;
  /**
   * 月次数剩余
   */
  private Long monthCountSurplus;
  private Date createTime;
  private Date updateTime;

}
