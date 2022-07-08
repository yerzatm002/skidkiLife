package kz.meirambekuly.skidkilife.web.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.meirambekuly.skidkilife.services.*;
import kz.meirambekuly.skidkilife.services.SaleService;
import kz.meirambekuly.skidkilife.utilities.Constants;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.salesDtos.SaleCreatorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.PRIVATE_ENDPOINT + "/establishment")
@RequiredArgsConstructor
public class EstablishmentCRMController {

    private final EstablishmentService establishmentService;
    private final SaleService saleService;

    @PostMapping("/createSale")
    @ApiOperation(value = "Create sale for establishment")
    public ResponseEntity<?> createSale(@ApiParam(value = "sale creator dto")
                                        @RequestBody SaleCreatorDto dto){
        return ResponseEntity.ok(saleService.save(dto));
    }

    @GetMapping("/findSalesOfEstablishment")
    @ApiOperation(value = "get all sales of logged in Establishment")
    public ResponseEntity<?> findAllSalesOfEstablishment(){
        return ResponseEntity.ok(saleService.findAllByPrincipal());
    }

    @PutMapping("/updateSale")
    @ApiOperation(value = "update details of sale for establishment")
    public ResponseEntity<?> updateSale(@ApiParam(value = "saleId") @RequestParam("saleId") Long id,
                                        @RequestBody SaleCreatorDto dto){
        return ResponseEntity.ok(saleService.update(id, dto));
    }

    @DeleteMapping("/deleteSale")
    @ApiOperation(value = "delete sale of establishment")
    public ResponseEntity<?> deleteSale(@ApiParam(value = "sale ID") @RequestParam("saleId") Long id){
        saleService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getLoggedEstablishment")
    @ApiParam(value = "get logged establishment information")
    public ResponseEntity<?> getLoggedEstablishmentInformation(){
        return ResponseEntity.ok(establishmentService.getLoggedUserInformation());
    }

    @GetMapping("/getToken")
    @ApiParam(value = "get new token")
    public ResponseEntity<?> getToken(){
        return ResponseEntity.ok(establishmentService.getToken());
    }

    @PutMapping("/updateEstablishment")
    @ApiParam(value = "update establishment information")
    public ResponseEntity<?> updateEstablishment(@ApiParam(value = "establishment details")
                                                 @RequestBody EstablishmentCreatorDto dto){
        return ResponseEntity.ok(establishmentService.update(dto));
    }

}
