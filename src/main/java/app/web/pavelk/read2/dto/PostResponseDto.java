package app.web.pavelk.read2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String postName;
    private String description;
    private String userName;
    private String subReadName;
    private Long subReadId;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
    private String vote;
}
