package app.web.pavelk.read2.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    USER_EXISTS("Such a user already exists.", "Такой пользователь уже существует."),
    USER_NOT_FOUND("User not found with name - %s.", null),
    MAIL_SENDING("Exception occurred when sending mail to %s.", null),
    INIT_JKS("Exception occurred while loading keystore.", null),
    RETRIEVING_KEY("Exception occurred while retrieving %s key from keystore.", null),
    SUB_NOT_FOUND("The sub is not found %s.", null),

    ERROR("Error", "Ошибка");
    private final String bodyEn;
    private final String bodyRu;
}
