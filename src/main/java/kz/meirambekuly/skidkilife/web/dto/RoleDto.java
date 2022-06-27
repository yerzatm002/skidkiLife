package kz.meirambekuly.skidkilife.web.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RoleDto {
    private Long id;
    private String name;
}
