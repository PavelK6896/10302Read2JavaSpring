package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.mapper.PostMapper;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.Post;
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
    public ResponseEntity<Void> createPost(PostRequestDto postRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        return null;
    }

    @Override
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<Post> posts = postRepository.findPageEntityGraphAll(pageable);
        List<Long> postIds = posts.getContent().stream().map(Post::getPostId).collect(Collectors.toList());
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
        PageImpl<PostResponseDto> postResponseDto = new PageImpl<>(postResponseDtoList, posts.getPageable(), posts.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }


    @Override
    public ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(Long subredditId) {
        return null;
    }

    @Override
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(String username) {
        return null;
    }
}
