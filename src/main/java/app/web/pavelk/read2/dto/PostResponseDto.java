package app.web.pavelk.read2.dto;

import app.web.pavelk.read2.schema.projection.PostResponseProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto implements PostResponseProjection {
    private Long id;
    private String postName;
    private String description;
    private String userName;
    private String subReadName;
    private Long subReadId;
    private Integer voteCount;
    private Integer commentCount;
    private LocalDateTime duration;
    private String vote;
}
