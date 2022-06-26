package app.web.pavelk.read2.dto;

import app.web.pavelk.read2.schema.projection.PostResponseProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Post data")
public class PostResponseDto implements PostResponseProjection {
    @Schema(description = "id post")
    private Long id;
    @Schema(description = "name post")
    private String postName;
    @Schema(description = "text post")
    private String description;
    @Schema(description = "name user")
    private String userName;
    @Schema(description = "name sub read")
    private String subReadName;
    @Schema(description = "id sub read")
    private Long subReadId;
    @Schema(description = "count votes")
    private Integer voteCount;
    @Schema(description = "count comment")
    private Integer commentCount;
    @Schema(description = "creat data")
    private LocalDateTime duration;
    @Schema(description = "current vote")
    private String vote;
}
