package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.service.SubReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/subreddit")
public class SubReadController {

    private final SubReadService subReadService;

    @PostMapping
    @Operation(description = "Creat sub.")
    public ResponseEntity<SubReadDto> createSubRead(@RequestBody SubReadDto subReadDto) {
        return subReadService.createSubRead(subReadDto);
    }

    @GetMapping
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Operation(description = "Get page sub.")
    public ResponseEntity<Page<SubReadDto>> getPageSubRead(@Parameter(hidden = true) Pageable pageable) {
        return subReadService.getPageSubRead(pageable);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get sub.")
    public ResponseEntity<SubReadDto> getSubReadById(@PathVariable Long id) {
        return subReadService.getSubReadById(id);
    }

}
