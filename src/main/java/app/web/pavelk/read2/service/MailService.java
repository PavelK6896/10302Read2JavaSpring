package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.NotificationEmail;
import app.web.pavelk.read2.exceptions.SpringRedditException;
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
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    public String email;

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
            log.info("Activation email sent!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringRedditException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }
    }
}
