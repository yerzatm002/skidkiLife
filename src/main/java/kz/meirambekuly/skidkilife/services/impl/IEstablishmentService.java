package kz.meirambekuly.skidkilife.services.impl;

import kz.meirambekuly.skidkilife.services.EstablishmentService;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;

public class IEstablishmentService implements EstablishmentService {
    @Override
    public ResultDto<?> create(EstablishmentCreatorDto dto) {
        return null;
    }

    @Override
    public ResultDto<?> update(EstablishmentCreatorDto dto) {
        return null;
    }

    @Override
    public EstablishmentDto findById(Long id) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
