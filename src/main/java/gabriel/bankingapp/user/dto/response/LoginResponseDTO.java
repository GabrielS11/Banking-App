package gabriel.bankingapp.user.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;


public class LoginResponseDTO {
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public LoginResponseDTO(String token) {
        Token = token;
    }
}
