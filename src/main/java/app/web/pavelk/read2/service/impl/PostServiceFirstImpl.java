package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.exceptions.Read2Exception;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VoteType;
import app.web.pavelk.read2.service.PostService;
import app.web.pavelk.read2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.POST_NOT_FOUND;
import static app.web.pavelk.read2.exceptions.ExceptionMessage.USER_NOT_FOUND;

@Slf4j(topic = "post-service-first")
@Service
@RequiredArgsConstructor
public class PostServiceFirstImpl implements PostService {

    private final PostRepository postRepository;
    private final SubReadRepository subReadRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        log.debug("createPost");
        SubRead subRead = subReadRepository
                .findByName(postRequestDto.getSubReadName())
                .orElseThrow(() -> new Read2Exception(ExceptionMessage.SUB_NOT_FOUND.getMessage()
                        .formatted(postRequestDto.getSubReadName())));
        postRepository.save(Post.builder()
                .postName(postRequestDto.getPostName())
                .description(postRequestDto.getDescription())
                .createdDate(LocalDateTime.now())
                .user(userService.getCurrentUserFromDB())
                .subRead(subRead)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PostResponseDto> getPost(Long id) {
        log.debug("getPost by id");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND.getMessage().formatted(id)));
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
    public ResponseEntity<Page<PostResponseDto>> getPagePostsBySubReadId(Long subredditId, Pageable pageable) {
        log.debug("getPostsBySub ");
        pageable = getDefaultPageable(pageable);
        SubRead subRead = subReadRepository.findById(subredditId)
                .orElseThrow(() -> new Read2Exception(ExceptionMessage.SUB_NOT_FOUND.getMessage().formatted(subredditId)));
        Page<PostResponseDto> page = postRepository.findPageBySubRead(subRead, pageable).map(this::getPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Page<PostResponseDto>> getPagePostsByUsername(String username, Pageable pageable) {
        log.debug("getPostsBySub ");
        pageable = getDefaultPageable(pageable);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage().formatted(username)));
        Page<PostResponseDto> page = postRepository.findPageByUser(user, pageable).map(this::getPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @Nullable
    private String getVote(Post post) {
        if (userService.isAuthenticated()) {
            return voteRepository.getTypeByUser(post, userService.getCurrentUserFromDB())
                    .map(VoteType::toString).orElse(null);
        }
        return null;
    }

    private PostResponseDto getPostDto(Post post) {
        Integer count = voteRepository.getCount(post);
        return PostResponseDto.builder()
                .id(post.getId())
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
