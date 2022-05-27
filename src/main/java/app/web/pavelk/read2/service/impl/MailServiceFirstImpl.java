package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.NotificationEmail;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.SubReadException;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceFirstImpl implements MailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username:}")
    public String email;
    @Value("${app.notification:false}")
    public boolean appNotification;
    @Value("${host:}")
    private String host;

    @Override
    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(email);
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            String stringStyle = "<div style=\"" +
                    "color:green;" +
                    "border: 2px solid green;" +
                    "padding: 5px;" +
                    "text-align: center;" +
                    "\">";
            mimeMessageHelper.setText(
                    "<html><body>" + stringStyle + "Read 1.</div><div>"
                            + notificationEmail.getBody() + "</div></body></html>", true);
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            log.error(ExceptionMessage.MAIL_SENDING.getBodyEn().formatted(notificationEmail.getRecipient()), e);
            throw new SubReadException(ExceptionMessage.MAIL_SENDING.getBodyEn().formatted(notificationEmail.getRecipient()), e);
        }
    }

    @Override
    public void sendCommentNotification(String message, User user) {
        if (appNotification) {
            sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
        }
    }

    @Override
    public void sendAuthNotification(String email, String token) {
        sendMail(NotificationEmail.builder()
                .subject("Please Activate your Account")
                .recipient(email)
                .body("Thank you for signing up to Spring Reddit, " +
                        "please click on the below url to activate your account: " +
                        host + "/api/read2/api/auth/accountVerification/" + token
                ).build());
    }
}
