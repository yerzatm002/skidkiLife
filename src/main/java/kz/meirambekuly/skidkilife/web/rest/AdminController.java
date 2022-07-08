package kz.meirambekuly.skidkilife.web.rest;

import io.swagger.annotations.ApiParam;
import kz.meirambekuly.skidkilife.services.*;
import kz.meirambekuly.skidkilife.utilities.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.ADMIN_ENDPOINT)
@RequiredArgsConstructor
public class AdminController {

    private final EstablishmentService establishmentService;

    @DeleteMapping("/deleteEstablishment")
    @ApiParam(value = "delete establishment by id")
    public ResponseEntity<?> deleteEstablishment(@ApiParam(value = "establishment id")
                                                 @RequestParam("establishmentId") Long id){
        establishmentService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getEstablishment")
    @ApiParam(value = "get establishment by phone number")
    public ResponseEntity<?> getEstablishmentByPhoneNumber(@ApiParam(value = "establishment phone number")
                                                               @RequestParam("phoneNumber") String phoneNumber){
        return ResponseEntity.ok(establishmentService.findByPhoneNumber(phoneNumber));
    }

}
