package kz.meirambekuly.skidkilife.services.impl;


import com.google.common.collect.Sets;
import kz.meirambekuly.skidkilife.config.PasswordEncoder.PasswordEncoder;
import kz.meirambekuly.skidkilife.config.jwt.Jwt;
import kz.meirambekuly.skidkilife.constants.Errors;
import kz.meirambekuly.skidkilife.constants.EstablishmentTypes;
import kz.meirambekuly.skidkilife.entity.*;
import kz.meirambekuly.skidkilife.repositories.*;
import kz.meirambekuly.skidkilife.services.EstablishmentService;
import kz.meirambekuly.skidkilife.specifications.EmployeeSpecifications;
import kz.meirambekuly.skidkilife.specifications.EstablishmentSpecifications;
import kz.meirambekuly.skidkilife.specifications.SMSVerificationSpecifications;
import kz.meirambekuly.skidkilife.utilities.Constants;
import kz.meirambekuly.skidkilife.utilities.ErrorMessages;
import kz.meirambekuly.skidkilife.utilities.ObjectMapper;
import kz.meirambekuly.skidkilife.utilities.SecurityUtils;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.RoleDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablsihmentLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class IEstablishmentService implements EstablishmentService{

    private final EstablishmentRepository establishmentRepository;
    private final SmsVerificationRepository smsVerificationRepository;
    private final RoleRepository roleRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final CityRepository cityRepository;
    private final EmployeeRepository employeeRepository;

    @Async
    public Future<String> getOtp(String phoneNumber){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "https://smsc.kz/sys/send.php?login=info@sulu.life&psw=c708adb7a37585ca85de3ba573feb71aa1e57cf2&phones={phoneNumber}&mes=code&call=1";
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("phoneNumber", phoneNumber);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(url);
        try{
            HttpEntity<String> response = restTemplate.exchange(
                    uri.buildAndExpand(urlParams).toUri(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            String fullString = response.getBody().substring(response.getBody().indexOf("CODE"));
            String partString = fullString.substring(6);
            partString = partString.replaceFirst("", "");
//        String fulString= response.body.substring(response.body.indexOf("CODE"));
            return AsyncResult.forValue(partString);
        }catch (Exception e){
            System.out.println("Ocurred eerrroorr " + e);
        }
        return AsyncResult.forValue(null);
    }

    @Override
    public ResultDto<?> register(EstablishmentCreatorDto dto) throws ExecutionException, InterruptedException {
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
           Future<String> code = getOtp(dto.getPhoneNumber());
           SmsVerification smsVerification = SmsVerification.builder()
                   .phoneNumber(dto.getPhoneNumber())
                   .code(code.get())
                   .build();
           smsVerification = smsVerificationRepository.save(smsVerification);
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
                    .city(cityRepository.findById(dto.getCityId()).get())
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
    public ResultDto<?> login(EstablsihmentLoginDto dto) {
        Optional<Establishment> establishment = establishmentRepository.findOne(EstablishmentSpecifications
                .findByPhoneNumberAndPassword(dto.getPhoneNumber(), PasswordEncoder.hashcode(dto.getPassword())));
        Optional<SmsVerification> verification = smsVerificationRepository
                .findOne(SMSVerificationSpecifications.findVerificationByPhoneNumber(dto.getPhoneNumber()));
        if (verification.isPresent()) {
            return ResultDto.builder()
                    .isSuccess(false)
                    .HttpStatus(HttpStatus.UNAUTHORIZED.value())
                    .errorMessage("Verify phone number")
                    .build();
        }
        if(establishment.isPresent()){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + establishment.get().getRole().getName()));
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(Constants.PREFIX + Jwt.generateJwt(dto.getPhoneNumber(),authorities))
                    .build();
        }
        return null;
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

    @Transactional
    @Override
    public ResultDto<?> changePassword(String oldPassword, String newPassword) {
        Optional<Establishment> establishment = establishmentRepository.findOne(EstablishmentSpecifications
                .findByPhoneNumber(SecurityUtils.getCurrentUserLogin()));
        if(establishment.isPresent()){
            if(PasswordEncoder.hashcode(oldPassword).equals(establishment.get().getPassword())){
                if(!PasswordEncoder.hashcode(newPassword).equals(establishment.get().getPassword())){
                    establishment.get().setPassword(PasswordEncoder.hashcode(newPassword));
                    return ResultDto.builder()
                            .isSuccess(true)
                            .HttpStatus(HttpStatus.OK.value())
                            .data(establishment.get().getId())
                            .build();
                }
                return ResultDto.builder()
                        .isSuccess(false)
                        .HttpStatus(HttpStatus.NO_CONTENT.value())
                        .errorMessage(Errors.NOT_VALID_FIELDS)
                        .build();
            }
            return ResultDto.builder()
                    .isSuccess(false)
                    .HttpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorMessage(Errors.INCORRECT_PASSWORD)
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(Errors.UNAUTHORIZED)
                .build();
    }

    @Override
    public ResultDto<?> resetPassword(String phoneNumber) {
        return null;
    }

    @Override
    public ResultDto<?> loginAdmin(String phoneNumber, String password) {
        Optional<Employee> employee = employeeRepository.findOne(
                EmployeeSpecifications.findEmployeeByPhoneNumberAndPassword(phoneNumber, PasswordEncoder.hashcode(password)));
        if(employee.isPresent()){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + employee.get().getRole().getName()));
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(Constants.PREFIX + Jwt.generateJwt(phoneNumber, authorities))
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.BAD_REQUEST.value())
                .errorMessage(ErrorMessages.incorrectPassword())
                .build();
    }
}
