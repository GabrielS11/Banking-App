package gabriel.bankingapp.user.service;

import gabriel.bankingapp.core.exception.InvalidOtpException;
import gabriel.bankingapp.core.exception.ResourceNotFoundException;
import gabriel.bankingapp.core.security.JwtTokenProvider;
import gabriel.bankingapp.notification.service.NotificationService;
import gabriel.bankingapp.user.dto.*;
import gabriel.bankingapp.user.dto.response.LoginResponseDTO;
import gabriel.bankingapp.user.dto.response.OtpRequiredResponseDTO;
import gabriel.bankingapp.user.entity.User;
import gabriel.bankingapp.user.repository.UserRepository;
import gabriel.bankingapp.utils.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import gabriel.bankingapp.utils.OtpService;

import java.util.List;
import java.util.Optional;


@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordResetService passwordResetService;

    public Object loginUser(LoginRequestDTO loginRequest){
        //Faz autenticaçao com o Spring Security, recebe as credenciais e delega um AuthenticationProvider configurado, SecurityConfig -> DaoAuthenticationProvider -> UserDetailsServiceImpl
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found after authentication"));

        if (user.isActive_twoFA()){

            String otp = otpService.generateAndSaveOtp(user);
            notificationService.sendOtpEmail(user, otp);

            return new OtpRequiredResponseDTO("2FA is required. OTP sent to your email.");
        } else {
            String jwt = jwtTokenProvider.generateToken(authentication);
            return new LoginResponseDTO(jwt);
        }
    }

    public LoginResponseDTO verifyOtp(OtpVerifyRequestDTO dto){
        User user = otpService.validateOtp(dto.getEmail(), dto.getOtp());

        String roleName = "ROLE_" + user.getRole().name();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                (null),
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        return new LoginResponseDTO(jwt);
    }

    public void resendOtp(ResendOtpRequestDTO dto){
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email: " + dto.getEmail()));

        if (!user.isActive_twoFA()){
            throw new InvalidOtpException("2FA is not enable for this user");
        }

        String newOtp = otpService.generateAndSaveOtp(user);

        notificationService.sendOtpEmail(user, newOtp);
    }

    public void forgotPassword(ForgotPasswordRequestDTO dto){
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

        if (optionalUser.isEmpty()){
            return;
        }

        User user = optionalUser.get();

        String token = passwordResetService.generateAndStore(user);
        notificationService.sendPasswordResetEmail(user, token);
    }


    public void resetPassword(String token, String newPassword){
        passwordResetService.validateTokenAndChangePassword(token, newPassword);
    }
}
