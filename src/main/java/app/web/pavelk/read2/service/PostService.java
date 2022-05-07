package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {

    ResponseEntity<Void> createPost(PostRequestDto postRequestDto);

    ResponseEntity<PostResponseDto> getPost(Long postId);

    ResponseEntity<List<PostResponseDto>> getAllPosts();

    ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(Long subredditId);

    ResponseEntity<List<PostResponseDto>> getPostsByUsername(String username);
}
