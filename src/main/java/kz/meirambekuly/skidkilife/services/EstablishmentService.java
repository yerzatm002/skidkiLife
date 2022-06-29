package kz.meirambekuly.skidkilife.services;

import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDetailsDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;

import java.util.List;

public interface EstablishmentService {
    ResultDto<?> register(EstablishmentCreatorDto dto);

    ResultDto<?> getToken();

    ResultDto<?> getLoggedUserInformation();

    ResultDto<?> update (EstablishmentCreatorDto dto);

    ResultDto<?> findById (Long id);

    ResultDto<?> findByPhoneNumber(String phoneNumber);

    ResultDto<?> activateEstablishment(String phoneNumber, String code);

    ResultDto<?> checkPhoneNumber(String phoneNumber);

    ResultDto<?> findAll();

    ResultDto<?> findAllByName(String name);

    ResultDto<?> findByAddress(String address);

    ResultDto<?> findByType(String type);

    void remove (Long id);

    //TODO: send OTP to the phoneNumber
    //TODO: changePassword
    //TODO: resetPassword
}
