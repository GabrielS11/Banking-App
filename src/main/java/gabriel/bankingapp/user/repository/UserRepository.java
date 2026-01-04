package gabriel.bankingapp.user.repository;

import gabriel.bankingapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    //Isto cria uma query: SELECT * FROM users WHERE email= ?
    Optional<User> findByEmail(String email);
}
