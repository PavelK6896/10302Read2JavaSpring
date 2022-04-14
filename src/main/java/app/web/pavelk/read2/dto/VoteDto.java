package app.web.pavelk.read2.dto;

import app.web.pavelk.read2.schema.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private VoteType voteType;
    private Long postId;
}
