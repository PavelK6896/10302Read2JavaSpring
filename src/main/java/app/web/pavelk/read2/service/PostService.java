package app.web.pavelk.read2.service;


import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.exceptions.SubredditNotFoundException;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.Subreddit;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VoteType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        log.info("createPost");
        Subreddit subreddit = subredditRepository.findByName(postRequestDto.getSubReadName())
                .orElseThrow(() -> new SubredditNotFoundException("The sub is not found " + postRequestDto.getSubReadName()));

        postRepository.save(Post.builder().postName(postRequestDto.getPostName()).description(postRequestDto.getDescription())
                .createdDate(LocalDateTime.now()).user(authService.getCurrentUser()).subreddit(subreddit).build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private String getVote(Post post) {
        if (authService.isLoggedIn()) {
            return voteRepository.getTypeByUser(post, authService.getCurrentUser())
                    .map(VoteType::toString).orElse(null);
        }
        return null;
    }

    private PostResponseDto getPostDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subReadName(post.getSubreddit().getName())
                .subReadId(post.getSubreddit().getId())
                .voteCount(voteRepository.getCount(post))
                .commentCount(commentRepository.findByPost(post).size())
                .duration(post.getCreatedDate().toString())
                .vote(getVote(post)).build();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<PostResponseDto> getPost(Long id) {
        log.info("getPost");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found id " + id.toString()));
        return ResponseEntity.status(HttpStatus.OK).body(getPostDto(post));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {

        List<PostResponseDto> collect = postRepository.findAll().stream().map(this::getPostDto).collect(toList());
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(Long subredditId) {
        log.info("getPostsBySubreddit");
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not id " + subredditId.toString()));
        List<PostResponseDto> collect = postRepository.findAllBySubreddit(subreddit).stream().map(this::getPostDto).collect(toList());
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(String username) {
        log.info("getPostsBySubreddit");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found " + username));
        List<PostResponseDto> collect = postRepository.findByUser(user).stream().map(this::getPostDto).collect(toList());
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }
}
