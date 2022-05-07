package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.PostService;
import app.web.pavelk.read2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j(topic = "post-service-query")
@Service
@RequiredArgsConstructor
public class PostServiceQueryImpl implements PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final UserService userService;

    @Override
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> postList = (List<PostResponseDto>) (List<?>) postRepository.findPostList(userService.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }

    @Override
    @Transactional
    public ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(Long subredditId) {
        List<PostResponseDto> postBySubredditId = (List<PostResponseDto>) (List<?>) postRepository.findPostBySubredditId(subredditId, userService.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(postBySubredditId);

    }

    @Override
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(String username) {
        return null;
    }

}
