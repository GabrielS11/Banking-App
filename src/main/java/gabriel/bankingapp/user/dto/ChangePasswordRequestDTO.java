package gabriel.bankingapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequestDTO {
    @NotBlank(message = "Password atual é obrigatória.")
    private String currentPassword;

    @NotBlank(message = "Nova password é obrigatória.")
    @Size(min = 8, message = "Password deve ter pelo menos 8 caracteres.")
    private String newPassword;

    // --- Getters e Setters ---

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
