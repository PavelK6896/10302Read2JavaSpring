package app.web.pavelk.read2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "sub read response data")
public class SubReadDto {
    @Schema(description = "id sub read")
    private Long id;
    @NotNull
    @Schema(description = "name sub read")
    private String name;
    @Schema(description = "text sub read")
    private String description;
    @Schema(description = "count post")
    private Integer numberOfPosts;
}
