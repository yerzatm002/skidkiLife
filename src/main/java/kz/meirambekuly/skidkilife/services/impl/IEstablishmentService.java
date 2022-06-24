package kz.meirambekuly.skidkilife.services.impl;

import kz.meirambekuly.skidkilife.config.PasswordEncoder.PasswordEncoder;
import kz.meirambekuly.skidkilife.config.jwt.Jwt;
import kz.meirambekuly.skidkilife.constants.Errors;
import kz.meirambekuly.skidkilife.constants.EstablishmentTypes;
import kz.meirambekuly.skidkilife.entity.Establishment;
import kz.meirambekuly.skidkilife.entity.WorkSchedule;
import kz.meirambekuly.skidkilife.repositories.EstablishmentRepository;
import kz.meirambekuly.skidkilife.repositories.RoleRepository;
import kz.meirambekuly.skidkilife.repositories.SmsVerificationRepository;
import kz.meirambekuly.skidkilife.repositories.WorkScheduleRepository;
import kz.meirambekuly.skidkilife.services.EstablishmentService;
import kz.meirambekuly.skidkilife.specifications.EstablishmentSpecifications;
import kz.meirambekuly.skidkilife.utilities.ErrorMessages;
import kz.meirambekuly.skidkilife.utilities.ObjectMapper;
import kz.meirambekuly.skidkilife.utilities.SecurityUtils;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDetailsDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IEstablishmentService implements EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final SmsVerificationRepository smsVerificationRepository;
    private final RoleRepository roleRepository;
    private final WorkScheduleRepository workScheduleRepository;

    @Override
    public ResultDto<?> register(EstablishmentCreatorDto dto) {
        Establishment establishment = establishmentRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (Objects.nonNull(establishment)) {
            return ResultDto.builder()
                    .isSuccess(false)
                    .HttpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorMessage(ErrorMessages.userWithPhoneNumberExists(dto.getPhoneNumber()))
                    .build();
        }
        List<String> types = Arrays.stream(EstablishmentTypes.values()).map(EstablishmentTypes::name).collect(Collectors.toList());
        if (types.contains(dto.getType().toUpperCase())) {
            Set<WorkSchedule> workSchedules = new HashSet<>();
            for (Long temp : dto.getWorkScheduleId()) {
                Optional<WorkSchedule> workSchedule = workScheduleRepository.findById(temp);
                if (workSchedule.isEmpty()) {
                    return ResultDto.builder()
                            .isSuccess(false)
                            .HttpStatus(HttpStatus.BAD_REQUEST.value())
                            .errorMessage("Not valid work schedule id")
                            .build();
                }
                workSchedule.ifPresent(workSchedules::add);
            }
            Establishment newEstablishment = Establishment.builder()
                    .name(dto.getName())
                    .phoneNumber(dto.getPhoneNumber())
                    .password(PasswordEncoder.hashcode(dto.getPassword()))
                    .address(dto.getAddress())
                    .longitude(dto.getLongitude())
                    .latitude(dto.getLatitude())
                    .type(dto.getType())
                    .description(dto.getDescription())
                    .fromWorkSchedule(dto.getFromWorkSchedule())
                    .toWorkSchedule(dto.getToWorkSchedule())
                    .workSchedule(workSchedules)
                    .build();
            newEstablishment = establishmentRepository.save(newEstablishment);
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(newEstablishment.getId())
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.BAD_REQUEST.value())
                .errorMessage("Not valid type!")
                .build();
    }

//    @Override
//    public ResultDto<?> getToken() {
//        if (SecurityUtils.isAuthenticated()) {
//            String token = Jwt.generateJwt(SecurityUtils.getCurrentUserLogin(), SecurityUtils.getAuthorities());
//            return ResultDto.builder()
//                    .isSuccess(true)
//                    .HttpStatus(HttpStatus.OK.value())
//                    .data(token)
//                    .build();
//        }
//        return ResultDto.builder()
//                .isSuccess(false)
//                .HttpStatus(HttpStatus.UNAUTHORIZED.value())
//                .errorMessage(Errors.UNAUTHORIZED)
//                .build();
//    }

    @Override
    public ResultDto<?> getLoggedUserInformation() {
       Establishment establishment =  establishmentRepository.findByPhoneNumber(SecurityUtils.getCurrentUserLogin());
       if(Objects.nonNull(establishment)){
           EstablishmentDto dto = ObjectMapper.convertToEstablishmentDto(establishment);
           return ResultDto.builder()
                   .isSuccess(true)
                   .HttpStatus(HttpStatus.OK.value())
                   .data(dto)
                   .build();
       }
       return ResultDto.builder()
               .isSuccess(false)
               .HttpStatus(HttpStatus.UNAUTHORIZED.value())
               .errorMessage(Errors.UNAUTHORIZED)
               .build();
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
    public EstablishmentDto findByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Override
    public ResultDto<?> activateEstablishment(String phoneNumber, String code) {
        return null;
    }

    @Override
    public ResultDto<?> checkPhoneNumber(String phoneNumber) {
        return null;
    }

    @Override
    public List<EstablishmentDetailsDto> findAll() {
        return null;
    }

    @Override
    public List<EstablishmentDetailsDto> findAllByName(String name) {
        return null;
    }

    @Override
    public List<EstablishmentDetailsDto> findByAddress(String address) {
        return null;
    }

    @Override
    public List<EstablishmentDetailsDto> findByType(String type) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
