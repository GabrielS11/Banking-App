package gabriel.bankingapp.notification.service;


import gabriel.bankingapp.user.entity.User;

public interface NotificationService {
    void sendOtpEmail(User user, String otp);
    void sendPasswordResetEmail(User user, String resetToken);
}
