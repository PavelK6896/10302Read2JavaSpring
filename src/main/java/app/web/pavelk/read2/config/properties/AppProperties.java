package app.web.pavelk.read2.config.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
@Getter
@Setter(AccessLevel.PACKAGE)
public class AppProperties {

    private boolean notificationComment;
    private boolean notificationSingUp;
    private String host;
    private boolean bigValidators;

}
