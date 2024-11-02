package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description
 * @create 2024/11/2 18:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuRechargeEntity {

    private String userId;
    private Long sku;
    /**
     * 业务仿重ID - 外部透传的，确保幂等
     */
    private String outBusinessNo;
}
