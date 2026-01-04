package gabriel.bankingapp.user.dto.response;

import gabriel.bankingapp.user.entity.User;
import gabriel.bankingapp.user.enums.UserRole;
import gabriel.bankingapp.user.enums.UserStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class UserMeResponseDTO {
    private String name;
    private String nif;
    private String email;
    private String phoneNumber;
    private String address;
    private UserRole role;
    private UserStatus status;
    private boolean active_twoFA;

    // --- Construtor Vazio (Obrigatório) ---
    public UserMeResponseDTO() {
    }

    public UserMeResponseDTO(User user) {
        this.name = user.getName();
        this.nif = user.getNif();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.active_twoFA = user.isActive_twoFA();
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public boolean isActive_twoFA() {
        return active_twoFA;
    }

    public void setActive_twoFA(boolean active_twoFA) {
        this.active_twoFA = active_twoFA;
    }
}
