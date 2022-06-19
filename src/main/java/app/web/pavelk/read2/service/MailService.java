package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.NotificationEmail;
import app.web.pavelk.read2.schema.User;

/**
 * Service mail notifications.
 */
public interface MailService {
    /**
     * Send abstract message.
     *
     * @param notificationEmail abstract data
     */
    void sendMail(NotificationEmail notificationEmail);

    /**
     * Send message new comment for post a user.
     *
     * @param message text
     * @param user    person
     */
    void sendCommentNotification(String message, User user);

    /**
     * Send message for approval new account.
     *
     * @param email mail address user
     * @param token approval token
     */
    void sendAuthNotification(String email, String token);
}
