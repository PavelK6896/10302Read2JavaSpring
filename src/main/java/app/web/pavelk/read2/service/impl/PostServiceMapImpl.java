package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.exceptions.Read2Exception;
import app.web.pavelk.read2.mapper.PostMapper;
import app.web.pavelk.read2.repository.CommentRepository;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.repository.SubReadRepository;
import app.web.pavelk.read2.repository.VoteRepository;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.VoteType;
import app.web.pavelk.read2.service.PostService;
import app.web.pavelk.read2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.POST_NOT_FOUND;

@Slf4j(topic = "post-service-map")
@Service
@RequiredArgsConstructor
public class PostServiceMapImpl implements PostService {

    private final PostRepository postRepository;
    private final SubReadRepository subReadRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    @Override
    @Transactional
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        SubRead subRead = subReadRepository
                .findByName(postRequestDto.getSubReadName())
                .orElseThrow(() -> new Read2Exception(ExceptionMessage.SUB_NOT_FOUND
                        .getMessage().formatted(postRequestDto.getSubReadName())));
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
    @Transactional
    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND.getMessage().formatted(postId)));
        Integer count = voteRepository.getCount(post);
        return ResponseEntity.status(HttpStatus.OK).body(PostResponseDto.builder()
                .id(post.getId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subReadName(post.getSubRead().getName())
                .subReadId(post.getSubRead().getId())
                .voteCount(count == null ? 0 : count)
                .commentCount(commentRepository.findByPost(post).size())
                .duration(post.getCreatedDate())
                .vote(getVote(post)).build());
    }

    @Override
    @Transactional
    public ResponseEntity<Page<PostResponseDto>> getPagePosts(Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<Post> posts = postRepository.findPageEntityGraphAll(pageable);
        PageImpl<PostResponseDto> postResponseDto = mapPostPage(posts);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<Page<PostResponseDto>> getPagePostsBySubReadId(Long subredditId, Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<Post> posts = postRepository.findAllBySubredditEntityGraphAll(subredditId, pageable);
        PageImpl<PostResponseDto> postResponseDto = mapPostPage(posts);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<Page<PostResponseDto>> getPagePostsByUsername(String username, Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<Post> posts = postRepository.findByUserEntityGraphAll(username, pageable);
        PageImpl<PostResponseDto> postResponseDto = mapPostPage(posts);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    private PageImpl<PostResponseDto> mapPostPage(Page<Post> posts) {
        List<Long> postIds = posts.getContent().stream().map(Post::getId).toList();
        Map<Long, Integer> postIdVoteCountMap = voteRepository.findListPostIdVoteCount(postIds).stream()
                .collect(Collectors.toMap(f -> (Long) f.get(0), f -> ((Long) f.get(1)).intValue()));
        Map<Long, Integer> postIdCommentCountMap = commentRepository.findListTuplePostIdCommentCount(postIds).stream()
                .collect(Collectors.toMap(f -> (Long) f.get(0), f -> ((Long) f.get(1)).intValue()));
        Map<Long, String> postIdVoteTypeMap = voteRepository.findListTuplePostIdVoteType(postIds, userService.getUserId()).stream()
                .collect(Collectors.toMap(f -> (Long) f.get(0), f -> String.valueOf(f.get(1))));

        PostMapper.MapContextPosts mapContextPosts = PostMapper.MapContextPosts.builder()
                .postIdVoteCountMap(postIdVoteCountMap)
                .postIdCommentCountMap(postIdCommentCountMap)
                .postIdVoteTypeMap(postIdVoteTypeMap)
                .build();
        List<PostResponseDto> postResponseDtoList = postMapper.toDtoList(posts.getContent(), mapContextPosts);
        return new PageImpl<>(postResponseDtoList, posts.getPageable(), posts.getTotalPages());
    }

    @Nullable
    private String getVote(Post post) {
        if (userService.isAuthenticated()) {
            return voteRepository.getTypeByUser(post, userService.getCurrentUserFromDB())
                    .map(VoteType::toString).orElse(null);
        }
        return null;
    }

}
