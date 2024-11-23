package site.mufen.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mufen
 * @Description
 * @create 2024/11/10 21:36
 */
@Data
public class UserActivityAccountResponseDTO implements Serializable {
    private Integer totalCount;
    private Integer totalCountSurplus;
    private Integer monthCount;
    private Integer monthCountSurplus;
    private Integer dayCount;
    private Integer dayCountSurplus;
}
