package site.mufen.infrastructure.elasticsearch;

import site.mufen.infrastructure.elasticsearch.po.UserRaffleOrder;

import java.util.List;

public interface IElasticSearchUserRaffleOrderDao {
    List<UserRaffleOrder> queryUserRaffleOrderList();
}
