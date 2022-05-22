package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.exceptions.SubredditNotFoundException;
import app.web.pavelk.read2.mapper.PostMapper;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.Subreddit;
import app.web.pavelk.read2.schema.VoteType;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.PostService;
import app.web.pavelk.read2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j(topic = "post-service-map")
@Service
@RequiredArgsConstructor
public class PostServiceMapImpl implements PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    @Override
    @Transactional
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        Subreddit subreddit = subredditRepository
                .findByName(postRequestDto.getSubReadName())
                .orElseThrow(() -> new SubredditNotFoundException("The sub is not found " + postRequestDto.getSubReadName()));
        postRepository.save(Post.builder()
                .postName(postRequestDto.getPostName())
                .description(postRequestDto.getDescription())
                .createdDate(LocalDateTime.now())
                .user(authService.getCurrentUser())
                .subreddit(subreddit)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @Transactional
    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found id " + postId));
        Integer count = voteRepository.getCount(post);
        return ResponseEntity.status(HttpStatus.OK).body(PostResponseDto.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subReadName(post.getSubreddit().getName())
                .subReadId(post.getSubreddit().getId())
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
    public ResponseEntity<Page<PostResponseDto>> getPagePostsBySubreddit(Long subredditId, Pageable pageable) {
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
        List<Long> postIds = posts.getContent().stream().map(Post::getPostId).toList();
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

    private String getVote(Post post) {
        if (authService.isLoggedIn()) {
            return voteRepository.getTypeByUser(post, authService.getCurrentUser())
                    .map(VoteType::toString).orElse(null);
        }
        return null;
    }

}
