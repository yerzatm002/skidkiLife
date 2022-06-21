package kz.meirambekuly.skidkilife.web.dto.establishmentDtos;

import kz.meirambekuly.skidkilife.web.dto.WorkScheduleDto;
import lombok.*;

import java.sql.Time;
import java.util.Calendar;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel(value = "EstablishmentDTO", description = "establishment info")
public class EstablishmentDto{
    private Long id;
    private String phoneNumber;
    private String password;
    private String name;
    private Calendar createdDate;
    private Boolean isActivated;
    private Boolean isEnabled;
    private String address;
    private Double longitude;
    private Double latitude;
    private String type;
    private String description;
    private Time fromWorkSchedule;
    private Time toWorkSchedule;
    private Set<WorkScheduleDto> workScheduleDtoSet;
    private String image;
}
