package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.VoteDto;
import org.springframework.http.ResponseEntity;

public interface VoteService {

    ResponseEntity<Integer> vote(VoteDto voteDto);

}
