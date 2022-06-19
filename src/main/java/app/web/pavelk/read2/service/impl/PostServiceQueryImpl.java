package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.Read2Exception;
import app.web.pavelk.read2.mapper.PostMapper;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.repository.SubReadRepository;
import app.web.pavelk.read2.schema.projection.PostResponseProjection;
import app.web.pavelk.read2.service.PostService;
import app.web.pavelk.read2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j(topic = "post-service-query")
@Service
@RequiredArgsConstructor
public class PostServiceQueryImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostMapper postMapper;
    private final SubReadRepository subReadRepository;

    @Override
    @Transactional
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        if (!subReadRepository.existsSubReadByName(postRequestDto.getSubReadName())) {
            throw new Read2Exception(ExceptionMessage.SUB_NOT_FOUND.getMessage().formatted(postRequestDto.getSubReadName()));
        }
        postRepository.insertPost(postRequestDto, userService.getUser().getId(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @Transactional
    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        PostResponseProjection post = postRepository.findPostResponseProjectionById(postId, userService.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(postMapper.convertProjectionToDto(post));
    }

    @Override
    @Transactional
    public ResponseEntity<Page<PostResponseDto>> getPagePosts(Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<PostResponseDto> postList = (Page<PostResponseDto>) (Page<?>) postRepository
                .findPagePost(userService.getUserId(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }

    @Override
    @Transactional
    public ResponseEntity<Page<PostResponseDto>> getPagePostsBySubReadId(Long subredditId, Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<PostResponseDto> postBySubredditId = (Page<PostResponseDto>) (Page<?>) postRepository
                .findPostBySubredditId(subredditId, userService.getUserId(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(postBySubredditId);
    }

    @Override
    @Transactional
    public ResponseEntity<Page<PostResponseDto>> getPagePostsByUsername(String username, Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<PostResponseDto> postByUsername = (Page<PostResponseDto>) (Page<?>) postRepository
                .findPostByUsername(username, userService.getUserId(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(postByUsername);
    }

}
