package site.mufen.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mufen
 * @Description skus商品购物车请求参数
 * @create 2024/11/15 13:18
 */
@Data
public class SkuProductShopCartRequestDTO implements Serializable {
    private String userId;
    private Long sku;
}
