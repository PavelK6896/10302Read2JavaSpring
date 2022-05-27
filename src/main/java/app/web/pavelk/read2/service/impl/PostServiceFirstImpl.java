package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.exceptions.SubReadException;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VoteType;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j(topic = "post-service-first")
@Service
@RequiredArgsConstructor
public class PostServiceFirstImpl implements PostService {

    private final PostRepository postRepository;
    private final SubReadRepository subReadRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        log.debug("createPost");
        SubRead subRead = subReadRepository
                .findByName(postRequestDto.getSubReadName())
                .orElseThrow(() -> new SubReadException(ExceptionMessage.SUB_NOT_FOUND.getBodyEn().formatted(postRequestDto.getSubReadName())));
        postRepository.save(Post.builder()
                .postName(postRequestDto.getPostName())
                .description(postRequestDto.getDescription())
                .createdDate(LocalDateTime.now())
                .user(authService.getCurrentUser())
                .subRead(subRead)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PostResponseDto> getPost(Long id) {
        log.debug("getPost");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found id " + id));
        return ResponseEntity.status(HttpStatus.OK).body(getPostDto(post));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Page<PostResponseDto>> getPagePosts(Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<PostResponseDto> page = postRepository.findPage(pageable).map(this::getPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Page<PostResponseDto>> getPagePostsBySubreddit(Long subredditId, Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        log.debug("getPostsBySubreddit");
        SubRead subRead = subReadRepository.findById(subredditId)
                .orElseThrow(() -> new SubReadException(ExceptionMessage.SUB_NOT_FOUND.getBodyEn().formatted(subredditId)));
        Page<PostResponseDto> page = postRepository.findPageBySubRead(subRead, pageable).map(this::getPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Page<PostResponseDto>> getPagePostsByUsername(String username, Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        log.debug("getPostsBySubreddit");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found " + username));
        Page<PostResponseDto> page = postRepository.findPageByUser(user, pageable).map(this::getPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    private String getVote(Post post) {
        if (authService.isLoggedIn()) {
            return voteRepository.getTypeByUser(post, authService.getCurrentUser())
                    .map(VoteType::toString).orElse(null);
        }
        return null;
    }

    private PostResponseDto getPostDto(Post post) {
        Integer count = voteRepository.getCount(post);
        return PostResponseDto.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subReadName(post.getSubRead().getName())
                .subReadId(post.getSubRead().getId())
                .voteCount(count == null ? 0 : count)
                .commentCount(commentRepository.findByPost(post).size())
                .duration(post.getCreatedDate())
                .vote(getVote(post)).build();
    }
}
