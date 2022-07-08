package kz.meirambekuly.skidkilife.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CityDto", description = "cities information")
public class CityDto {
    private Long id;
    private String name;
}