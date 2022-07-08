package kz.meirambekuly.skidkilife.web.dto.establishmentDtos;

import kz.meirambekuly.skidkilife.web.dto.CityDto;
import kz.meirambekuly.skidkilife.web.dto.WorkScheduleDto;
import lombok.*;

import java.sql.Time;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstablishmentDetailsDto {
    private String phoneNumber;
    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
    private String type;
    private String description;
    private Time fromWorkSchedule;
    private Time toWorkSchedule;
    private CityDto cityDto;
    private Set<WorkScheduleDto> workSchedule;
}
