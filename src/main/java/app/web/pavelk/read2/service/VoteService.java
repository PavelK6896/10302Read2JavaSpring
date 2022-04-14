package app.web.pavelk.read2.service;


import app.web.pavelk.read2.dto.VoteDto;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.repository.VoteRepository;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.Vote;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public ResponseEntity<Integer> vote(VoteDto voteDto) {
        log.info("vote");
        if (authService.isLoggedIn()) {
            User currentUser = authService.getCurrentUser();
            Post post = postRepository.findById(voteDto.getPostId())
                    .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
            Optional<Vote> optionalVote = voteRepository.getTypeByUserPostId(voteDto.getPostId(), currentUser);

            if (optionalVote.isPresent()) {
                Vote vote1 = optionalVote.get();
                vote1.setVoteType(voteDto.getVoteType());
                voteRepository.save(vote1);
            } else {
                voteRepository.save(Vote.builder().post(post).user(currentUser).voteType(voteDto.getVoteType()).build());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(voteRepository.getCount(post));
        } else {
            throw new ResponseStatusException(UNAUTHORIZED, "not auth");
        }
    }
}
