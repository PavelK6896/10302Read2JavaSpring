package app.web.pavelk.read2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data login")
public class LoginRequest {
    @Schema(description = "username", example = "admin", required = true)
    private String username;
    @Schema(description = "password", example = "1", required = true)
    private String password;
}
