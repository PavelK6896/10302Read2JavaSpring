package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.NotificationEmail;
import app.web.pavelk.read2.schema.User;

public interface MailService {
    void sendMail(NotificationEmail notificationEmail);

    void sendCommentNotification(String message, User user);

    void sendAuthNotification(String email, String token);
}
