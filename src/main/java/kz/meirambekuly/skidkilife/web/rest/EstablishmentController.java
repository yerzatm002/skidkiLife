package kz.meirambekuly.skidkilife.web.rest;

import kz.meirambekuly.skidkilife.utilities.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.PUBLIC_ENDPOINT + "/establishment")
@RequiredArgsConstructor
public class EstablishmentController {



    @GetMapping
    public String sayHello(){
        return "Hello";
    }


}
