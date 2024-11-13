package site.mufen.trigger.api.dto;

import lombok.Data;

/**
 * @author mufen
 * @Description
 * @create 2024/11/10 21:36
 */
@Data
public class UserActivityAccountResponseDTO {
    private Integer totalCount;
    private Integer totalCountSurplus;
    private Integer monthCount;
    private Integer monthCountSurplus;
    private Integer dayCount;
    private Integer dayCountSurplus;
}
