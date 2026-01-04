package gabriel.bankingapp.user.controller;


import gabriel.bankingapp.user.dto.*;
import gabriel.bankingapp.user.dto.response.LoginResponseDTO;
import gabriel.bankingapp.user.service.AuthService;
import gabriel.bankingapp.user.service.UserService;
import gabriel.bankingapp.utils.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody RegisterRequestDTO dto){ //O @Valid ativa as validaçoes do DTO, e o @RequestBody converte JSON para o DTO
        userService.createUser(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User was registered successfully.");
    }


    //Usamos o ResponseEntity<?> porque a resposta pode ser um LoginResponseDTO (com token) ou um OtpRequiredResponseDTO (com mensagem)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO dto){
        try {
            Object response = authService.loginUser(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponseDTO> verifyOtp(@Valid @RequestBody OtpVerifyRequestDTO dto) {
        LoginResponseDTO response = authService.verifyOtp(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@Valid @RequestBody ResendOtpRequestDTO dto){
        authService.resendOtp(dto);

        return ResponseEntity.ok("New OTP code was sent to you're email.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto){

        authService.forgotPassword(dto);
        return ResponseEntity.ok("If email exists, a reset-link was sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto){
        authService.resetPassword(dto.getToken(), dto.getPassword());
        return ResponseEntity.ok("Password updated with success");
    }

}


// /register; /login -> POST; /verify-otp -> POST; /forgot-password -> POST; /reset-password -> POST; /resend-otp -> POST
