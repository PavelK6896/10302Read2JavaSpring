package app.web.pavelk.read2.service;


import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.dto.NotificationEmail;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.mapper.CommentMapper;
import app.web.pavelk.read2.repository.CommentRepository;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Autowired
    private CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;
    @Value("${host-url:https}")
    private String hostUrl;

    public ResponseEntity<Void> createComment(CommentsDto commentsDto) {
        log.info("createComment");
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("No post " + commentsDto.getPostId().toString()));
        User currentUser = authService.getCurrentUser();
        commentRepository.save(commentMapper.map(commentsDto, post, currentUser));

        String stringMessageMail = currentUser.getUsername() + " posted a comment on your post. "
                + hostUrl + "/view-post/" + post.getPostId();

        sendCommentNotification(stringMessageMail, post.getUser());
        return ResponseEntity.status(CREATED).build();
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername()
                + " Commented on your post", user.getEmail(), message));
    }

    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(Long postId) {
        log.info("getAllCommentsForPost");
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("Not found post " + postId.toString()));
        return ResponseEntity.status(OK).body(
                commentRepository.findByPost(post).stream()
                        .map(commentMapper::mapToDto).collect(toList()));
    }

    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(String userName) {
        log.info("getAllCommentsForUser");
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found " + userName));
        return ResponseEntity.status(OK)
                .body(commentRepository.findAllByUser(user)
                        .stream()
                        .map(commentMapper::mapToDto)
                        .collect(toList()));
    }
}
