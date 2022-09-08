package kz.meirambekuly.skidkilife.web.dto.establishmentDtos;


import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EstablishmentLoginDTO", description = "mandatory fields to login establishment")
public class EstablsihmentLoginDto {
    private String phoneNumber;
    private String password;
}
