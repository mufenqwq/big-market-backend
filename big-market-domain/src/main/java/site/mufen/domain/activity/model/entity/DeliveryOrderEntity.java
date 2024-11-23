package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 22:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderEntity {

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 外部透穿Id 保证幂等
     */
    private String outBusinessNo;
}
