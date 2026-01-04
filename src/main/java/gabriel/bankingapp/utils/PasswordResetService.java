package gabriel.bankingapp.utils;

import gabriel.bankingapp.core.exception.InvalidTokenException;
import gabriel.bankingapp.core.exception.ResourceNotFoundException;
import gabriel.bankingapp.user.entity.User;
import gabriel.bankingapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateAndStore(User user){
        String token = UUID.randomUUID().toString();

        String redisKey = "reset:" + token;

        redisTemplate.opsForValue().set(redisKey, user.getId().toString(), Duration.ofHours(1));
        return token;
    }


    public void validateTokenAndChangePassword(String token, String newPassword){
        String redisKey = "reset:" + token;


        String userIdString = redisTemplate.opsForValue().get(redisKey);

        if (userIdString == null){
            throw new InvalidTokenException("Token is not valid or expired.");
        }

        Long userId = Long.parseLong(userIdString);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        String newPasswordHash = passwordEncoder.encode(newPassword);

        user.setPassword_hash(newPasswordHash);


        userRepository.save(user);
        redisTemplate.delete(redisKey);
    }
}

