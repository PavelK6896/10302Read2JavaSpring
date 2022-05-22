package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PostService extends CommonService {

    ResponseEntity<Void> createPost(PostRequestDto postRequestDto);

    ResponseEntity<PostResponseDto> getPost(Long postId);

    ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable);

    ResponseEntity<Page<PostResponseDto>> getPostsBySubreddit(Long subredditId, Pageable pageable);

    ResponseEntity<Page<PostResponseDto>> getPostsByUsername(String username, Pageable pageable);
}
