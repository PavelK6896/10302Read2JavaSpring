package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.VoteDto;
import app.web.pavelk.read2.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Integer> vote(@RequestBody VoteDto voteDto) {
        return voteService.vote(voteDto);
    }

}
