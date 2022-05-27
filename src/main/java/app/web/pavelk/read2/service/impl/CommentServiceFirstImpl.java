package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.mapper.CommentMapper;
import app.web.pavelk.read2.repository.CommentRepository;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.CommentService;
import app.web.pavelk.read2.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j(topic = "comment-service-first")
@Service
@RequiredArgsConstructor
public class CommentServiceFirstImpl implements CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;
    @Value("${host-url:https}")
    private String hostUrl;

    @Override
    @Transactional
    public ResponseEntity<Void> createComment(CommentsDto commentsDto) {
        log.debug("createComment");
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("No post " + commentsDto.getPostId().toString()));
        User currentUser = authService.getCurrentUserDB();
        commentRepository.save(commentMapper.map(commentsDto, post, currentUser));

        String stringMessageMail = "%s posted a comment on your post. %s/view-post/%s "
                .formatted(currentUser.getUsername(), hostUrl, post.getPostId());
        mailService.sendCommentNotification(stringMessageMail, post.getUser());
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(Long postId) {
        log.debug("getAllCommentsForPost");
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("Not found post " + postId));
        return ResponseEntity.status(OK).body(
                commentRepository.findByPost(post)
                        .stream()
                        .map(commentMapper::mapToDto)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(String userName) {
        log.debug("getAllCommentsForUser");
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found " + userName));
        return ResponseEntity.status(OK)
                .body(commentRepository.findAllByUser(user)
                        .stream()
                        .map(commentMapper::mapToDto)
                        .toList());
    }
}
