package site.mufen.infrastructure.persistent.dao.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mufen
 * @Description 任务表
 * @create 2024/11/4 10:34
 */
@Data
public class Task {
    /**
     * 自增Id
     */
    private String id;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 消息主题
     */
    private String topic;
    /**
     * 消息Id
     */
    private String messageId;
    /**
     * 消息主体
     */
    private String message;
    /**
     * 人物状态 create-创建 completed-完成 fail-失败
     */
    private String state;
    private Date createTime;
    private Date updateTime;
}
