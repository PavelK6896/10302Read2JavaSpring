package app.web.pavelk.read2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private String subReadName;
    private String postName;
    private String description;
}
