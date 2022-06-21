package kz.meirambekuly.skidkilife.web.dto.salesDtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleCreatorDto{
    private String title;
    private String description;
    private Double cost;
    private Integer discount;
    private Date fromDate;
    private Date toDate;
    private Long establishmentId;
}
