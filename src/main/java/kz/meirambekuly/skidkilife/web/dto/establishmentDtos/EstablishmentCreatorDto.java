package kz.meirambekuly.skidkilife.web.dto.establishmentDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.sql.Time;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EstablishmentCreatorDTO", description = "mandatory fields to create establishment")
public class EstablishmentCreatorDto{
    private String phoneNumber;
    private String password;
    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
    private String type;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
    private Time fromWorkSchedule;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
    private Time toWorkSchedule;
    private Set<Long> workScheduleId;
}
