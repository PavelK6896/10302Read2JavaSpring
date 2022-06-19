package app.web.pavelk.read2.exceptions;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum ExceptionMessage {

    USER_EXISTS("Such a user already exists.", "Такой пользователь уже существует."),
    SUB_EXISTS("Sub read name %s exist.", null),
    MAIL_SENDING("Exception occurred when sending mail to %s.", null),
    INIT_JKS("Exception occurred while loading keystore.", null),
    RETRIEVING_KEY("Exception occurred while retrieving %s key from keystore.", null),
    USER_NOT_FOUND("User not found with name - %s.", null),
    SUB_NOT_FOUND("The sub is not found %s.", null),
    POST_NOT_FOUND("Post not found with id - %s", null),
    REFRESH_TOKEN_NOT_FOUND("Invalid refresh Token", null),
    ERROR("Error", "Ошибка");

    private final String bodyEn;
    private final String bodyRu;

    public String getMessage() {
        return this.bodyEn;
    }

    public String getMessage(short code) {
        if (code == 2) {
            return this.bodyRu;
        }
        return this.bodyEn;
    }

}
