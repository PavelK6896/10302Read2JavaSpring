package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.mapper.CommentMapper;
import app.web.pavelk.read2.repository.CommentRepository;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.CommentService;
import app.web.pavelk.read2.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j(topic = "comment-service-query")
@Service
@RequiredArgsConstructor
public class CommentServiceQueryImpl implements CommentService {

    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;
    @Value("${host-url:https}")
    private String hostUrl;

    @Override
    public ResponseEntity<Void> createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("No post " + commentsDto.getPostId().toString()));
        User currentUser = authService.getCurrentUserDB();
        commentRepository.save(commentMapper.map(commentsDto, post, currentUser));

        String stringMessageMail = "%s posted a comment on your post. %s/view-post/%s "
                .formatted(currentUser.getUsername(), hostUrl, commentsDto.getPostId());
        mailService.sendCommentNotification(stringMessageMail, currentUser);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(Long postId) {
        return ResponseEntity.status(OK).body(commentRepository.findCommentsDtoByPostId(postId));
    }

    @Override
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(String username) {
        return ResponseEntity.status(OK).body(commentRepository.findCommentsDtoByUserName(username));
    }

}
