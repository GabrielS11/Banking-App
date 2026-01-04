package gabriel.bankingapp.user.dto.response;


public class OtpRequiredResponseDTO {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OtpRequiredResponseDTO(String message) {
        this.message = message;
    }
}
