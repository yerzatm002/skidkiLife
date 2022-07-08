package kz.meirambekuly.skidkilife.web.rest;

import kz.meirambekuly.skidkilife.services.SaleService;
import kz.meirambekuly.skidkilife.utilities.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.PUBLIC_ENDPOINT + "/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(saleService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam("id") Long id){
        return ResponseEntity.ok(saleService.findById(id));
    }


}
