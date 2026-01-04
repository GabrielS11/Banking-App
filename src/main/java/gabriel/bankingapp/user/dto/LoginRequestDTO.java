package gabriel.bankingapp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


public class LoginRequestDTO {
    @NotBlank(message = "Email is Required.")
    @Email
    private String email;

    @NotBlank(message = "Password is Required.")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
