package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.RaffleActivitySku;

/**
 * @author mufen
 * @Description 商品sku Dao
 * @create 2024/11/1 18:20
 */
@Mapper
public interface IRaffleActivitySkuDao {

    RaffleActivitySku queryRaffleActivitySku(Long sku);
}
