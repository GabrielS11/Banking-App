package gabriel.bankingapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


public class ResetPasswordRequestDTO {
    @NotBlank(message = "Token is required.")
    private String token;

    @NotBlank(message = "New Password is required.")
    @Size(min = 8, message = "Password must have at least 8 characters.")
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
