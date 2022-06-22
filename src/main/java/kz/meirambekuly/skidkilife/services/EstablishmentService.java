package kz.meirambekuly.skidkilife.services;

import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;

public interface EstablishmentService {
    ResultDto<?> create (EstablishmentCreatorDto dto);
    ResultDto<?> update (EstablishmentCreatorDto dto);
    EstablishmentDto findById (Long id);
    void remove (Long id);
}
