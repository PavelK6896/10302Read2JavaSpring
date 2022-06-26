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
@Schema(description = "Post create data")
public class PostRequestDto {
    @Schema(description = "subReadName", example = "Technical", required = true)
    private String subReadName;
    @Schema(description = "postName", example = "postName", required = true)
    private String postName;
    @Schema(description = "description", example = "description", required = true)
    private String description;
}
