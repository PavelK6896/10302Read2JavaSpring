package app.web.pavelk.read2.dto;

import app.web.pavelk.read2.schema.VoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Vote create dto")
public class VoteDto {
    @Schema(description = "type vote")
    private VoteType voteType;
    @Schema(description = "id post")
    private Long postId;
}
