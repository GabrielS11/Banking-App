package gabriel.bankingapp.user.service;

import gabriel.bankingapp.core.exception.ResourceNotFoundException;
import gabriel.bankingapp.user.dto.ChangePasswordRequestDTO;
import gabriel.bankingapp.user.dto.RegisterRequestDTO;
import gabriel.bankingapp.user.dto.UserMeUpdateRequestDTO;
import gabriel.bankingapp.user.dto.response.UserMeResponseDTO;
import gabriel.bankingapp.user.entity.User;
import gabriel.bankingapp.user.enums.UserRole;
import gabriel.bankingapp.user.enums.UserStatus;
import gabriel.bankingapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(RegisterRequestDTO dto){
        User newUser = new User();

        newUser.setName(dto.getName());
        newUser.setNif(dto.getNif());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword_hash(passwordEncoder.encode(dto.getPassword()));
        newUser.setPhoneNumber(dto.getPhone_number());
        newUser.setAddress(dto.getAddress());
        newUser.setRole(UserRole.COSTUMER);
        newUser.setStatus(UserStatus.ACTIVE);
        newUser.setActive_twoFA(false);

        userRepository.save(newUser);
    }

    public UserMeResponseDTO getUserProfile(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserMeResponseDTO(user);
    }


    public UserMeResponseDTO updateUserProfile(String email, UserMeUpdateRequestDTO dto){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.setName(dto.getName());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());


        User updateUser = userRepository.save(user);

        return new UserMeResponseDTO(updateUser);
    }

    public String activateUser2fa(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (user.isActive_twoFA()){
            return "User FA is already active";
        } else {
            user.setActive_twoFA(true);
            userRepository.save(user);
            return "User FA was activated";
        }
    }

    public String deactivateUser2fa(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!user.isActive_twoFA()){
            return "User FA is already deactivated";
        } else {
            user.setActive_twoFA(false);
            userRepository.save(user);
            return "User FA was deactivated";
        }
    }

    public String changePassword(String email, ChangePasswordRequestDTO dto){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword_hash())) {
            return "Password is incorrect";
        }

        String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword_hash(newPasswordHash);

        userRepository.save(user);
        return "Password is changed!";
    }
}

