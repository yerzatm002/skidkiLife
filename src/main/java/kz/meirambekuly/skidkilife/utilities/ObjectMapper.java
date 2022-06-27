package kz.meirambekuly.skidkilife.utilities;

import com.ibm.icu.text.Transliterator;
import kz.meirambekuly.skidkilife.entity.Establishment;
import kz.meirambekuly.skidkilife.entity.Role;
import kz.meirambekuly.skidkilife.entity.Sale;
import kz.meirambekuly.skidkilife.entity.WorkSchedule;
import kz.meirambekuly.skidkilife.web.dto.RoleDto;
import kz.meirambekuly.skidkilife.web.dto.WorkScheduleDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDetailsDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleDto;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class ObjectMapper {

    public static String convertCyryllicToLatin(String text) {
        Transliterator transliterator = Transliterator.getInstance("Cyrillic-Latin");
        return transliterator.transliterate(text);
    }

    public static RoleDto convertToRoleDto(Role role){
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static WorkScheduleDto convertToWorkSchedulesDto(WorkSchedule workSchedule) {
        return WorkScheduleDto.builder()
                .id(workSchedule.getId())
                .day(workSchedule.getDay()).build();
    }

    public static EstablishmentDetailsDto convertToEstablishmentDetailsDto(Establishment establishment){
        return EstablishmentDetailsDto.builder()
                .name(establishment.getName())
                .phoneNumber(establishment.getPhoneNumber())
                .address(establishment.getAddress())
                .description(establishment.getDescription())
                .longitude(establishment.getLongitude())
                .latitude(establishment.getLatitude())
                .type(establishment.getType())
                .fromWorkSchedule(establishment.getFromWorkSchedule())
                .toWorkSchedule(establishment.getToWorkSchedule())
                .workSchedule(establishment.getWorkSchedule()
                        .stream()
                        .map(ObjectMapper::convertToWorkSchedulesDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static EstablishmentDto convertToEstablishmentDto (Establishment establishment){
        return EstablishmentDto.builder()
                .id(establishment.getId())
                .phoneNumber(establishment.getPhoneNumber())
                .password(establishment.getPassword())
                .name(establishment.getName())
                .createdDate(establishment.getCreatedDate())
                .isActivated(establishment.getIsActivated())
                .isEnabled(establishment.getIsEnabled())
                .address(establishment.getAddress())
                .longitude(establishment.getLongitude())
                .latitude(establishment.getLatitude())
                .type(establishment.getType())
                .description(establishment.getDescription())
                .fromWorkSchedule(establishment.getFromWorkSchedule())
                .toWorkSchedule(establishment.getToWorkSchedule())
                .image(establishment.getImage())
                .workScheduleDtoSet(establishment.getWorkSchedule()
                                .stream()
                        .map(ObjectMapper::convertToWorkSchedulesDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static SaleDto convertToSaleDto (Sale sale){
        return SaleDto.builder()
                .id(sale.getId())
                .title(sale.getTitle())
                .description(sale.getDescription())
                .cost(sale.getCost())
                .discount(sale.getDiscount())
                .fromDate(sale.getFromDate())
                .toDate(sale.getToDate())
                .image(sale.getImage())
                .establishment(convertToEstablishmentDetailsDto(sale.getEstablishment()))
                .createdDate(sale.getCreatedDate())
                .build();
    }

}
