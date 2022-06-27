package kz.meirambekuly.skidkilife.services;

import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleDto;


public interface SaleService {
    ResultDto<?> save (SaleCreatorDto dto);
    ResultDto<?> update (Long id, SaleCreatorDto dto);
    SaleDto findById (Long id);
    void remove (Long id);
    ResultDto<?> findAll();
    ResultDto<?> findAllByPrincipal();
}
