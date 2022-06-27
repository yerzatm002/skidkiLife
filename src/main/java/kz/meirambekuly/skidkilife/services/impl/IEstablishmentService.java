package kz.meirambekuly.skidkilife.services.impl;

import kz.meirambekuly.skidkilife.config.PasswordEncoder.PasswordEncoder;
import kz.meirambekuly.skidkilife.config.jwt.Jwt;
import kz.meirambekuly.skidkilife.constants.Errors;
import kz.meirambekuly.skidkilife.constants.EstablishmentTypes;
import kz.meirambekuly.skidkilife.entity.Establishment;
import kz.meirambekuly.skidkilife.entity.SmsVerification;
import kz.meirambekuly.skidkilife.entity.WorkSchedule;
import kz.meirambekuly.skidkilife.repositories.EstablishmentRepository;
import kz.meirambekuly.skidkilife.repositories.RoleRepository;
import kz.meirambekuly.skidkilife.repositories.SmsVerificationRepository;
import kz.meirambekuly.skidkilife.repositories.WorkScheduleRepository;
import kz.meirambekuly.skidkilife.services.EstablishmentService;
import kz.meirambekuly.skidkilife.specifications.EstablishmentSpecifications;
import kz.meirambekuly.skidkilife.specifications.SMSVerificationSpecifications;
import kz.meirambekuly.skidkilife.utilities.ErrorMessages;
import kz.meirambekuly.skidkilife.utilities.ObjectMapper;
import kz.meirambekuly.skidkilife.utilities.SecurityUtils;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

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

    @Override
    public ResultDto<?> getToken() {
        if (SecurityUtils.isAuthenticated()) {
            String token = Jwt.generateJwt(SecurityUtils.getCurrentUserLogin(), SecurityUtils.getAuthorities());
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(token)
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(Errors.UNAUTHORIZED)
                .build();
    }

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

    @Transactional
    @Override
    public ResultDto<?> update(EstablishmentCreatorDto dto) {
        List<String> types = Arrays.stream(EstablishmentTypes.values()).map(EstablishmentTypes::name).collect(Collectors.toList());
        Optional<Establishment> establishment = establishmentRepository.findOne(EstablishmentSpecifications
                .findByPhoneNumber(SecurityUtils.getCurrentUserLogin()));
        if(establishment.isPresent()){
            if(Objects.nonNull(dto.getName()) && !dto.getName().isEmpty() && !dto.getName().isBlank()){
                establishment.get().setName(dto.getName());
            }
            if(Objects.nonNull(dto.getAddress()) && !dto.getLatitude().isNaN() && !dto.getLongitude().isNaN()){
                establishment.get().setAddress(dto.getAddress());
                establishment.get().setLatitude(dto.getLatitude());
                establishment.get().setLongitude(dto.getLongitude());
            }
            if(Objects.nonNull(dto.getType()) && !dto.getType().isEmpty() && !dto.getType().isBlank() && types.contains(dto.getType())){
                establishment.get().setType(dto.getType());
            }
            if(Objects.nonNull(dto.getDescription()) && !dto.getDescription().isEmpty() && !dto.getDescription().isBlank()){
                establishment.get().setDescription(dto.getDescription());
            }
            if(Objects.nonNull(dto.getFromWorkSchedule())){
                establishment.get().setFromWorkSchedule(dto.getFromWorkSchedule());
            }
            if(Objects.nonNull(dto.getToWorkSchedule())){
                establishment.get().setToWorkSchedule(dto.getToWorkSchedule());
            }
            if(Objects.nonNull(dto.getWorkScheduleId())){
                establishment.get().setWorkSchedule(getWorkSchedule(dto.getWorkScheduleId()));
             }
            establishmentRepository.save(establishment.get());
            return ResultDto.builder()
                    .data(establishment.get().getId())
                    .HttpStatus(HttpStatus.OK.value())
                    .isSuccess(true)
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("UNAUTHORIZED")
                .build();
    }

    public Set<WorkSchedule> getWorkSchedule(Set<Long> workScheduleId){
        Set<WorkSchedule> workSchedules = new HashSet<>();
        for (Long temp : workScheduleId) {
            Optional<WorkSchedule> workSchedule = workScheduleRepository.findById(temp);
            if (workSchedule.isEmpty()) {
                return null;
            }
            workSchedule.ifPresent(workSchedules::add);
        }
        return workSchedules;
    }

    @Override
    public ResultDto<?> findById(Long id) {
        Optional<Establishment> establishmentObj = establishmentRepository.findById(id);
        if (establishmentObj.isPresent()) {
            EstablishmentDto establishmentDTO = ObjectMapper.convertToEstablishmentDto(establishmentObj.get());
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(establishmentDTO)
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.NOT_FOUND.value())
                .errorMessage(ErrorMessages.notFoundEstablishmentWithId(id))
                .build();
    }

    @Override
    public ResultDto<?> findByPhoneNumber(String phoneNumber) {
        Optional<Establishment> establishment = establishmentRepository.findOne(
                EstablishmentSpecifications.findByPhoneNumber(phoneNumber));
        if(establishment.isPresent()){
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(ObjectMapper.convertToEstablishmentDto(establishment.get()))
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.NOT_FOUND.value())
                .errorMessage("Establishment not found")
                .build();
    }

    @Override
    public ResultDto<?> activateEstablishment(String phoneNumber, String code) {
        Optional<SmsVerification> verification = smsVerificationRepository
                .findOne(SMSVerificationSpecifications.findVerificationByPhoneNumberAndCode(phoneNumber, code));
        if (verification.isPresent()) {
            smsVerificationRepository.deleteById(verification.get().getId());
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(403)
                .errorMessage("Not valid code")
                .build();
    }

    @Override
    public ResultDto<?> checkPhoneNumber(String phoneNumber) {
        if (nonNull(phoneNumber) && !phoneNumber.isEmpty() && !phoneNumber.isBlank()) {
            Establishment userObj = establishmentRepository.findByPhoneNumber(phoneNumber);
            if (nonNull(userObj)) {
                return ResultDto.builder()
                        .isSuccess(false)
                        .HttpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .errorMessage(ErrorMessages.userWithPhoneNumberExists(phoneNumber))
                        .build();
            }
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data("success")
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.NOT_ACCEPTABLE.value())
                .errorMessage(ErrorMessages.requiredFieldIsEmpty(phoneNumber))
                .build();
    }

    @Override
    public ResultDto<?> findAll() {
        List<EstablishmentDto> establishmentDtoList = establishmentRepository.findAll().stream().map(ObjectMapper::convertToEstablishmentDto).collect(Collectors.toList());
        return ResultDto.builder()
                .isSuccess(true)
                .HttpStatus(HttpStatus.OK.value())
                .data(establishmentDtoList)
                .build();
    }

    @Override
    public ResultDto<?> findAllByName(String name) {
        if (nonNull(name) && !name.isEmpty() && !name.isBlank()) {
            List<EstablishmentDto> establishments = establishmentRepository
                    .findAll(EstablishmentSpecifications
                            .findAllByName(ObjectMapper.convertCyryllicToLatin(name)))
                    .stream()
                    .map(ObjectMapper::convertToEstablishmentDto)
                    .collect(Collectors.toList());
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(establishments).build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @Override
    public ResultDto<?> findByAddress(String address) {
        if (nonNull(address) && !address.isEmpty() && !address.isBlank()) {
            List<EstablishmentDto> establishments = establishmentRepository
                    .findAll(EstablishmentSpecifications
                            .findAllByAddress(ObjectMapper.convertCyryllicToLatin(address)))
                    .stream()
                    .map(ObjectMapper::convertToEstablishmentDto)
                    .collect(Collectors.toList());
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(establishments).build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @Override
    public ResultDto<?> findByType(String type) {
        if (nonNull(type) && !type.isEmpty() && !type.isBlank()) {
            List<EstablishmentDto> establishments = establishmentRepository
                    .findAll(EstablishmentSpecifications
                            .findByType(ObjectMapper.convertCyryllicToLatin(type)))
                    .stream()
                    .map(ObjectMapper::convertToEstablishmentDto)
                    .collect(Collectors.toList());
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(establishments).build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @Transactional
    @Override
    public void remove(Long id) {
        establishmentRepository.deleteById(id);
    }
}
