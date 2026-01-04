package gabriel.bankingapp.core.security;


import gabriel.bankingapp.user.entity.User;
import gabriel.bankingapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Este é o método que o AuthenticationManager vai chamar.
     * "username" aqui é, na verdade, o email que passámos no LoginRequestDTO.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + username)
                );

        // 2. Converte o nosso "User" (da entity) para o "UserDetails" (do Spring)
        // O Spring Security usa este objeto para fazer a comparação de passwords.
        // O ArrayList vazio é para as "Roles/Autoridades", que podes implementar mais tarde.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword_hash(),
                new ArrayList<>()
        );
    }
}
