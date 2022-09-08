package kz.meirambekuly.skidkilife.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.meirambekuly.skidkilife.config.jwt.Jwt;
import kz.meirambekuly.skidkilife.constants.Errors;
import kz.meirambekuly.skidkilife.services.*;
import kz.meirambekuly.skidkilife.services.impl.IEstablishmentService;
import kz.meirambekuly.skidkilife.utilities.Constants;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablsihmentLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Constants.PUBLIC_ENDPOINT + "/auth")
@RequiredArgsConstructor
@ApiModel(value = "Auth Controller", description = "Authentication Controller")
public class AuthController {

    private final EstablishmentService establishmentService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ApiOperation(value = "user registration")
    public ResponseEntity<?> register (@ApiParam(value = "DTO for user registration")
                                           @RequestBody EstablishmentCreatorDto dto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(establishmentService.register(dto));
    }

    @PostMapping("/loginEstablishment")
    @ApiOperation(value = "login establishment")
    public ResponseEntity<?> login (@ApiParam(value = "DTO for establishment login")
                                        @RequestBody EstablsihmentLoginDto dto){
        return ResponseEntity.ok(establishmentService.login(dto));
    }

    @PostMapping("/checkPhoneNumber")
    @ApiOperation(value = "check phone number for an existing")
    public ResponseEntity<?> checkPhoneNumber(@ApiParam(value = "phone number")
                                           @RequestParam("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(establishmentService.checkPhoneNumber(phoneNumber));
    }

    @PostMapping("/activate")
    @ApiOperation(value = "Activate establishment by phone number")
    public ResponseEntity<?> activateEstablishment(@ApiParam(value = "phone number")
                                                   @RequestParam("phoneNumber") String phoneNumber,
                                                   @ApiParam(value = "sms code")
                                                   @RequestParam("code") String code){
        return ResponseEntity.ok(establishmentService.activateEstablishment(phoneNumber, code));
    }

    @PostMapping("/admin/login")
    @ApiOperation(value = "admin crm login")
    public ResponseEntity<?> loginAdmin(@RequestParam("phoneNumber") String phoneNumber,
                                        @RequestParam("password") String password){
        return ResponseEntity.ok(establishmentService.loginAdmin(phoneNumber,password));
    }
    
}
