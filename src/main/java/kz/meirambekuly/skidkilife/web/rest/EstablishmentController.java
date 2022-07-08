package kz.meirambekuly.skidkilife.web.rest;


import kz.meirambekuly.skidkilife.utilities.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kz.meirambekuly.skidkilife.services.*;


@RestController
@RequestMapping(Constants.PUBLIC_ENDPOINT + "/establishment")
@RequiredArgsConstructor
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllEstablishments(){
        return ResponseEntity.ok(establishmentService.findAll());
    }

    @GetMapping("/findByName")
    public ResponseEntity<?> findAllByName(@RequestParam("name") String name){
        return ResponseEntity.ok(establishmentService.findAllByName(name));
    }

    @GetMapping("/findByAddress")
    public ResponseEntity<?> findAllByAddress(@RequestParam("address") String address){
        return ResponseEntity.ok(establishmentService.findByAddress(address));
    }

    @GetMapping("/findByType")
    public ResponseEntity<?> findAllByType(@RequestParam("type") String type){
        return ResponseEntity.ok(establishmentService.findByType(type));
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam("id") Long id){
        return ResponseEntity.ok(establishmentService.findById(id));
    }


}
