package app.web.pavelk.read2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotBlank
    @NotNull
    @Size(min = 2)
    private String refreshToken;
    @NotNull
    private String username;
}
