package gabriel.bankingapp.notification.service;

import gabriel.bankingapp.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpEmailService implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(User user, String otp) {
        // Por agora, vamos apenas imprimir na consola para testar
        System.out.println("--- SIMULANDO ENVIO DE EMAIL ---");
        System.out.println("Para: " + user.getEmail());
        System.out.println("Assunto: O seu código de login 2FA");
        System.out.println("O seu código é: " + otp);
        System.out.println("---------------------------------");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@bankingapp.com");
        message.setTo(user.getEmail());
        message.setSubject("Your otp login code");
        message.setText("Olá " + user.getName() + ",\n\nO seu código de autenticação de dois fatores é: " + otp + "\n\nEste código expira em 5 minutos.");

        try{
            mailSender.send(message);
            System.out.println("Email sent to: " + user.getEmail());
        } catch (Exception e){
            System.out.println("Error sending email to: " + user.getEmail() + ":" + e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@bankingapp.com");
        message.setTo(user.getEmail());
        message.setSubject("Pedido de Reset de Password - BankingApp");
        // Ex: String resetUrl = "http://teufrontend.com/reset-password?token=" + resetToken;
        message.setText("Olá " + user.getName() + ",\n\nRecebemos um pedido para resetar a sua password.\n\n"
                + "O seu token de reset é: " + resetToken + "\n\n" //MUDAR PARA LINK
                + "Este token expira em 1 hora.\n\nSe não pediu isto, ignore este email.");

        try {
            mailSender.send(message);
            System.out.println("Email de reset enviado para: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de reset para " + user.getEmail() + ": " + e.getMessage());
        }
    }
}