package app.web.pavelk.read2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Comment data")
public class CommentsDto {
    @Schema(description = "Id comment")
    private Long id;
    @Schema(description = "Id post")
    private Long postId;
    @Schema(description = "Date create")
    private Instant createdDate;
    @Schema(description = "Body")
    private String text;
    @Schema(description = "User")
    private String userName;
}

