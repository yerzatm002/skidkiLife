package kz.meirambekuly.skidkilife.web.dto;

import lombok.*;


@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel(value = "WorkSchedulesDto", description = "Work schedule info")
public class WorkScheduleDto {
    private Long id;
    private String day;
}
