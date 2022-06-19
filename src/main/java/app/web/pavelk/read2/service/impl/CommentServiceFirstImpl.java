package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.mapper.CommentMapper;
import app.web.pavelk.read2.repository.CommentRepository;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.service.CommentService;
import app.web.pavelk.read2.service.MailService;
import app.web.pavelk.read2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.POST_NOT_FOUND;
import static app.web.pavelk.read2.exceptions.ExceptionMessage.USER_NOT_FOUND;
import static app.web.pavelk.read2.util.StaticField.POST_COMMENT_MESSAGE_NOTIFICATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j(topic = "comment-service-first")
@Service
@RequiredArgsConstructor
public class CommentServiceFirstImpl implements CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
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
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND.getMessage().formatted(commentsDto.getPostId())));
        User currentUser = userService.getCurrentUserFromDB();
        commentRepository.save(commentMapper.map(commentsDto, post, currentUser));

        String stringMessageMail = POST_COMMENT_MESSAGE_NOTIFICATION
                .formatted(currentUser.getUsername(), hostUrl, post.getId());
        mailService.sendCommentNotification(stringMessageMail, post.getUser());
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<Slice<CommentsDto>> getSliceCommentsForPost(Long postId, Pageable pageable) {
        log.debug("getAllCommentsForPost");
        pageable = getDefaultPageable(pageable);
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(POST_NOT_FOUND.getMessage().formatted(postId)));
        return ResponseEntity.status(OK).body(
                commentRepository.findByPost(post, pageable).map(commentMapper::mapToDto));
    }

    @Override
    public ResponseEntity<Slice<CommentsDto>> getSliceCommentsForUser(String userName, Pageable pageable) {
        log.debug("getAllCommentsForUser");
        pageable = getDefaultPageable(pageable);
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage().formatted(userName)));
        return ResponseEntity.status(OK)
                .body(commentRepository.findAllByUser(user, pageable).map(commentMapper::mapToDto));
    }
}
