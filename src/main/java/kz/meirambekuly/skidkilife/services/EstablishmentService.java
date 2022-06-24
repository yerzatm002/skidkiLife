package kz.meirambekuly.skidkilife.services;

import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDetailsDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;

import java.util.List;

public interface EstablishmentService {
    ResultDto<?> register(EstablishmentCreatorDto dto);

//    ResultDto<?> getToken();

    ResultDto<?> getLoggedUserInformation();

    ResultDto<?> update (EstablishmentCreatorDto dto);

    EstablishmentDto findById (Long id);

    EstablishmentDto findByPhoneNumber(String phoneNumber);

    ResultDto<?> activateEstablishment(String phoneNumber, String code);

    ResultDto<?> checkPhoneNumber(String phoneNumber);

    List<EstablishmentDetailsDto> findAll();

    List<EstablishmentDetailsDto> findAllByName(String name);

    List<EstablishmentDetailsDto> findByAddress(String address);

    List<EstablishmentDetailsDto> findByType(String type);

    void remove (Long id);
}
