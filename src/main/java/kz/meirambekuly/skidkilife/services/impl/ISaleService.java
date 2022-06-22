package kz.meirambekuly.skidkilife.services.impl;

import kz.meirambekuly.skidkilife.services.SaleService;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleDto;

public class ISaleService implements SaleService {
    @Override
    public ResultDto<?> save(SaleCreatorDto dto) {
        return null;
    }

    @Override
    public ResultDto<?> update(SaleCreatorDto dto) {
        return null;
    }

    @Override
    public SaleDto findById(Long id) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
