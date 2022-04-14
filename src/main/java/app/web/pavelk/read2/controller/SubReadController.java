package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.SubredditDto;
import app.web.pavelk.read2.service.SubReadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/subreddit")
public class SubReadController {

    private final SubReadService subReadService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return subReadService.save(subredditDto);
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return subReadService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        return subReadService.getSubreddit(id);
    }

}
