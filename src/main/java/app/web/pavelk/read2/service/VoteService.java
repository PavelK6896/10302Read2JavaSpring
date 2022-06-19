package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.VoteDto;
import org.springframework.http.ResponseEntity;

/**
 * Service for working with voices.
 */
public interface VoteService {

    /**
     * Create or update vote for post.
     *
     * @param voteDto new vote
     * @return all count votes for post
     */
    ResponseEntity<Integer> vote(VoteDto voteDto);

}
