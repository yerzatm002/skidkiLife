package kz.meirambekuly.skidkilife.web.dto.salesDtos;

import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDetailsDto;
import lombok.*;

import java.util.Calendar;
import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto{
    private Long id;
    private String title;
    private String description;
    private Double cost;
    private Integer discount;
    private Date fromDate;
    private Date toDate;
    private String image;
    private EstablishmentDetailsDto establishment;
    private Calendar createdDate;
}
