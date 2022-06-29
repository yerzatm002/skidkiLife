package kz.meirambekuly.skidkilife.web.rest;

import kz.meirambekuly.skidkilife.services.EstablishmentService;
import kz.meirambekuly.skidkilife.services.SaleService;
import kz.meirambekuly.skidkilife.utilities.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.PRIVATE_ENDPOINT + "/establishment")
@RequiredArgsConstructor
public class EstablishmentCRMController {

    private final EstablishmentService establishmentService;
    private final SaleService saleService;



}
