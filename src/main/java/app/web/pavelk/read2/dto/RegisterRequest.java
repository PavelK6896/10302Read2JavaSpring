package app.web.pavelk.read2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Email
    @NotNull
    private String email;

    @Size(min = 5)
    @NotNull
    @Pattern(regexp = "^([A-Za-z0-9]{2,}(\\\\-[a-zA-Z0-9])?)$")
    private String username;

    @Size(min = 5)
    @NotNull
    @Pattern(regexp = "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[@#$%^&+=])\\S{8,}\\z")
    private String password;

}
