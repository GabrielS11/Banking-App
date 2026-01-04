package gabriel.bankingapp.user.controller;


import gabriel.bankingapp.user.dto.ChangePasswordRequestDTO;
import gabriel.bankingapp.user.dto.UserMeUpdateRequestDTO;
import gabriel.bankingapp.user.dto.response.UserMeResponseDTO;
import gabriel.bankingapp.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;


    @GetMapping("/me/profile")
    public ResponseEntity<UserMeResponseDTO> getUserDetails(Authentication authentication){
        String userEmail = authentication.getName();

        UserMeResponseDTO userProfile = userService.getUserProfile(userEmail);

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<UserMeResponseDTO> changeUserDetails(Authentication authentication, @Valid @RequestBody UserMeUpdateRequestDTO dto){
        String userEmail = authentication.getName();

        UserMeResponseDTO updateUser = userService.updateUserProfile(userEmail, dto);

        return ResponseEntity.ok(updateUser);
    }

    @PostMapping("/me/2fa/activate")
    public ResponseEntity<String> activateUser2fa(Authentication authentication){
        String userEmail = authentication.getName();

        String response = userService.activateUser2fa(userEmail);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/me/2fa/deactivate")
    public ResponseEntity<String> deactivateUser2fa(Authentication authentication){
        String userEmail = authentication.getName();

        String response = userService.deactivateUser2fa(userEmail);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequestDTO dto){
        String userEmail = authentication.getName();

        String response = userService.changePassword(userEmail, dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}


/**
 * por questoes de segurança mudar o email, o nif e a password vao ficar em sitios diferentes
 *
 * nif -> users/me/nif (PUT) em que pedimos o nif e pedimos a password para confirmar se a pessoa e mesmo aquela
 * email -> users/me/email-change/request em que pede o novo email e a password atual e tem o users/me/email-change/confirm para confirmar o novo email
 * */