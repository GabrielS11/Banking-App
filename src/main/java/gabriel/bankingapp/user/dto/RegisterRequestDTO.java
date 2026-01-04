package gabriel.bankingapp.user.dto;

import gabriel.bankingapp.user.enums.UserRole;
import gabriel.bankingapp.user.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class RegisterRequestDTO {
    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "NIF is required.")
    @Size(min=9, max = 9, message = "NIF must have 9 digits.")
    private String nif;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email format is invalid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must have at least 8 characters.")
    private String password;

    @NotBlank(message = "Phone number is required.")
    private String phone_number;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
