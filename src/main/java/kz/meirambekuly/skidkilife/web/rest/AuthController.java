package kz.meirambekuly.skidkilife.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.meirambekuly.skidkilife.config.jwt.Jwt;
import kz.meirambekuly.skidkilife.constants.Errors;
import kz.meirambekuly.skidkilife.services.EstablishmentService;
import kz.meirambekuly.skidkilife.utilities.Constants;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import kz.meirambekuly.skidkilife.web.dto.establishmentDtos.EstablishmentCreatorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.PUBLIC_ENDPOINT + "/auth")
@RequiredArgsConstructor
@ApiModel(value = "Auth Controller", description = "Authentication Controller")
public class AuthController {

    private final EstablishmentService establishmentService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    @ApiOperation(value = "user registration")
    public ResponseEntity<?> register (@ApiParam(value = "DTO for user registration") @RequestBody EstablishmentCreatorDto dto){
        return ResponseEntity.ok(establishmentService.register(dto));
    }

    @PostMapping("/login")
    @ApiOperation(value = "user login")
    @Timed
    public ResponseEntity<?> login(@RequestParam("phoneNumber") String phoneNumber,
                                   @RequestParam("password") String password){
        if(phoneNumber.isEmpty() && password.isEmpty()){
            throw new RuntimeException("Not Valid Fields");
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
        if (authentication.isAuthenticated()) {
            String token = Jwt.generateJwt(authentication.getName(), authentication.getAuthorities());
            return ResponseEntity.ok(ResultDto.builder()
                    .isSuccess(true)
                    .HttpStatus(HttpStatus.OK.value())
                    .data(token)
                    .build());
        }
        return ResponseEntity.ok(ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(Errors.UNAUTHORIZED)
                .build());
    }

    @PostMapping("/checkPhoneNumber")
    @ApiOperation(value = "check phone number for an existing")
    public ResponseEntity checkPhoneNumber(@ApiParam(value = "phone number")
                                           @RequestParam("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(establishmentService.checkPhoneNumber(phoneNumber));
    }


}
