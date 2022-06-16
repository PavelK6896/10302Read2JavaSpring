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
@RequestMapping("/sub-read")
public class SubReadController {

    private final SubReadService subReadService;

    @Operation(description = "Get sub.")
    @GetMapping("/{id}")
    public ResponseEntity<SubReadDto> getSubReadById(@PathVariable Long id) {
        return subReadService.getSubReadById(id);
    }

    @Operation(description = "Get page sub.")
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @GetMapping
    public ResponseEntity<Page<SubReadDto>> getPageSubRead(@Parameter(hidden = true) Pageable pageable) {
        return subReadService.getPageSubRead(pageable);
    }

    @Operation(description = "Creat sub.")
    @PostMapping
    public ResponseEntity<SubReadDto> createSubRead(@RequestBody SubReadDto subReadDto) {
        return subReadService.createSubRead(subReadDto);
    }

    @Operation(description = "Get page sub like starts with.")
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Parameter(in = ParameterIn.QUERY, name = "startsWith", schema = @Schema(defaultValue = ""))
    @GetMapping("/starts-with")
    public ResponseEntity<Page<SubReadDto>> getPageSubReadLikeStartsWith(@RequestParam String startsWith, @Parameter(hidden = true) Pageable pageable) {
        return subReadService.getPageSubReadLikeStartsWith(pageable, startsWith);
    }

}
