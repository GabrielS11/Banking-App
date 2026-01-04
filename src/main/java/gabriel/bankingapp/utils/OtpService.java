package gabriel.bankingapp.utils;

import gabriel.bankingapp.core.exception.InvalidOtpException;
import gabriel.bankingapp.core.exception.ResourceNotFoundException;
import gabriel.bankingapp.user.entity.User;
import gabriel.bankingapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    private final Random random = new SecureRandom();
    public String generateAndSaveOtp(User user){
        String otp = generateOtp();

        String redisKey = "otp:" + user.getId().toString();
        redisTemplate.opsForValue().set(redisKey, otp, Duration.ofMinutes(5));
        return otp;
    }

    public User validateOtp(String email,String otp){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        String redisKey = "otp:" + user.getId().toString();

        String correctOtp = redisTemplate.opsForValue().get(redisKey);

        if (correctOtp == null){
            throw new InvalidOtpException("OTP expired. Please, try to login again.");
        }

        if(!correctOtp.equals(otp)){
            throw new InvalidOtpException("OTP invalid.");
        }

        redisTemplate.delete(redisKey);

        return user;
    }

    private String generateOtp(){
        int otpNumber = random.nextInt(1000000);
        return String.format("%06d", otpNumber);
    }
}
