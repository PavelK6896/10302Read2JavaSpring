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
@Schema(description = "property")
public class PropertyDto {
    @Schema(description = "status whether you need to confirm the mail address")
    private Boolean notificationSingUp;
    @Schema(description = "status whether to send an email notification about comments")
    private Boolean notificationComment;
    @Schema(description = "status whether to validate login and password up to 8 characters")
    private Boolean bigValidators;
    @Schema(description = "current address host")
    private String host;
}

