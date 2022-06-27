package kz.meirambekuly.skidkilife.services.impl;

import kz.meirambekuly.skidkilife.constants.Errors;
import kz.meirambekuly.skidkilife.entity.Establishment;
import kz.meirambekuly.skidkilife.entity.Sale;
import kz.meirambekuly.skidkilife.repositories.EstablishmentRepository;
import kz.meirambekuly.skidkilife.repositories.SaleRepository;
import kz.meirambekuly.skidkilife.services.SaleService;
import kz.meirambekuly.skidkilife.utilities.ObjectMapper;
import kz.meirambekuly.skidkilife.utilities.SecurityUtils;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ISaleService implements SaleService {

    private final SaleRepository saleRepository;
    private final EstablishmentRepository establishmentRepository;

    @Override
    public ResultDto<?> save(SaleCreatorDto dto) {
        if (SecurityUtils.isAuthenticated()) {
                Sale sale = Sale.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .cost(dto.getCost())
                        .discount(dto.getDiscount())
                        .fromDate(dto.getFromDate())
                        .toDate(dto.getToDate())
                        .establishment(establishmentRepository.findByPhoneNumber(SecurityUtils.getCurrentUserLogin()))
                        .build();
                sale = saleRepository.save(sale);
                return ResultDto.builder()
                        .isSuccess(true)
                        .HttpStatus(HttpStatus.OK.value())
                        .data(sale.getId())
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
    public ResultDto<?> update(Long id, SaleCreatorDto dto) {
        if(SecurityUtils.isAuthenticated()){
            Optional<Sale> sale = saleRepository.findById(id);
            if(sale.isPresent()){
                if(Objects.nonNull(dto.getTitle()) && !dto.getTitle().isEmpty() && !dto.getTitle().isBlank()){
                    sale.get().setTitle(dto.getTitle());
                }
                if(Objects.nonNull(dto.getCost()) && !dto.getCost().isNaN()){
                    sale.get().setCost(dto.getCost());
                }
                if(Objects.nonNull(dto.getDescription()) && !dto.getDescription().isEmpty() && !dto.getDescription().isBlank()){
                    sale.get().setDescription(dto.getDescription());
                }
                if(Objects.nonNull(dto.getDiscount()) && !dto.getDiscount().equals(0)){
                    sale.get().setDiscount(dto.getDiscount());
                }
                if(Objects.nonNull(dto.getFromDate())){
                    sale.get().setFromDate(dto.getFromDate());
                }
                if(Objects.nonNull(dto.getToDate())){
                    sale.get().setToDate(dto.getToDate());
                }
                saleRepository.save(sale.get());
                return ResultDto.builder()
                        .HttpStatus(HttpStatus.OK.value())
                        .isSuccess(true)
                        .data(sale.get().getId())
                        .build();
            }
            return ResultDto.builder()
                    .HttpStatus(HttpStatus.NOT_FOUND.value())
                    .isSuccess(false)
                    .errorMessage("NO_DATA_FOUND")
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(Errors.UNAUTHORIZED)
                .build();
    }

    @Override
    public SaleDto findById(Long id) {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isPresent()) {
            return ObjectMapper.convertToSaleDto(sale.get());
        }
        return SaleDto.builder().build();
    }

    @Async
    @Transactional
    @Override
    public void remove(Long id) {
        Optional<Establishment> establishment = Optional.ofNullable(establishmentRepository.findByPhoneNumber(SecurityUtils.getCurrentUserLogin()));
        if (establishment.isPresent()) {
            Optional<Sale> sale = saleRepository.findById(id);
            if (sale.isPresent()) {
                if (sale.get().getEstablishment().getId().equals(establishment.get().getId())) {
                    saleRepository.deleteById(id);
                    return;
                } else {
                    throw new RuntimeException(Errors.ERR_ACCESS_DENIED);
                }
            }
            throw new RuntimeException(Errors.ERR_VALIDATION);
        }
        throw new RuntimeException(Errors.NO_USER);
    }

    @Override
    public ResultDto<?> findAll() {
        List<SaleDto> saleDtoList = saleRepository.findAll().stream().map(ObjectMapper::convertToSaleDto).collect(Collectors.toList());
        return ResultDto.builder()
                .isSuccess(true)
                .data(saleDtoList)
                .HttpStatus(HttpStatus.OK.value())
                .build();
    }

    @Override
    public ResultDto<?> findAllByPrincipal() {
        if(SecurityUtils.isAuthenticated()){
            Establishment establishment = establishmentRepository.findByPhoneNumber(SecurityUtils.getCurrentUserLogin());
            List<SaleDto> saleDtoList = saleRepository.findAllByEstablishment(establishment)
                    .stream().map(ObjectMapper::convertToSaleDto).collect(Collectors.toList());
            return ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(saleDtoList)
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(Errors.UNAUTHORIZED)
                .build();    }
}
