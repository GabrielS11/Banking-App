package gabriel.bankingapp.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    /**
     * Cria um Bean global para o PasswordEncoder.
     * Isto permite-nos injetar (@Autowired) o PasswordEncoder
     * em qualquer serviço (ex: no AuthService) para fazer o hash
     * das passwords no registo e verificá-las no login.
     */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
