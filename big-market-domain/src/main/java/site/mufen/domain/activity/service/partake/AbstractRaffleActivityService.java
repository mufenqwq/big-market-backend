package site.mufen.domain.activity.service.partake;

import lombok.extern.slf4j.Slf4j;
import site.mufen.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import site.mufen.domain.activity.model.entity.ActivityEntity;
import site.mufen.domain.activity.model.entity.PartakeRaffleActivityEntity;
import site.mufen.domain.activity.model.entity.UserRaffleOrderEntity;
import site.mufen.domain.activity.model.valobj.ActivityStateVO;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.domain.activity.service.IRaffleActivityPartakeService;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;
import site.mufen.types.model.Response;

import java.util.Date;

/**
 * @author mufen
 * @Description 抽奖活动参与抽奖类
 * @create 2024/11/4 15:20
 */
@Slf4j
public abstract class AbstractRaffleActivityService implements IRaffleActivityPartakeService {

    protected final IActivityRepository activityRepository;

    public AbstractRaffleActivityService(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        // 0. 基础信息
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        String userId = partakeRaffleActivityEntity.getUserId();
        Date currentDate = new Date();

        // 1. 活动查询
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        // 校验活动状态
        if (!ActivityStateVO.open.equals(activityEntity.getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        // 校验活动日期 开始时间 < 当前时间 < 结束时间
        if (activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        // 3. 查询未被使用的活动参与订单记录
        UserRaffleOrderEntity userRaffleOrderEntity = activityRepository.queryNoUseRaffleOrder(partakeRaffleActivityEntity);
        if (null != userRaffleOrderEntity) {
            log.info("创建参与抽奖活动订单[已存在未消费] userId:{}, activityId:{}, userRaffleOrderEntity:{}", userId, activityEntity, userRaffleOrderEntity);
            return userRaffleOrderEntity;
        }

        // 3.账户额度过滤返回账户构建对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = this.doFilterAccount(userId, activityId, currentDate);

        // 4. 构建订单
        UserRaffleOrderEntity userRaffleOrder = this.buildUserRaffleOrder(userId, activityId, currentDate);

        // 5. 填充抽奖实体对象
        createPartakeOrderAggregate.setUserRaffleOrderEntity(userRaffleOrder);

        // 6. 保存聚合对象 一个领域内的一个聚合是一个事务操作
        activityRepository.saveCreatePartakeAggregate(createPartakeOrderAggregate);

        return userRaffleOrder;
    }

    protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate);

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);
}
