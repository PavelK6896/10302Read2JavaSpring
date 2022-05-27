package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.VoteDto;
import app.web.pavelk.read2.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    @Operation(description = "Click vote button up or down.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "vote body", required = true,
                    content = @Content(schema = @Schema(implementation = VoteDto.class))))
    @PostMapping
    public ResponseEntity<Integer> vote(@RequestBody VoteDto voteDto) {
        return voteService.vote(voteDto);
    }

}
