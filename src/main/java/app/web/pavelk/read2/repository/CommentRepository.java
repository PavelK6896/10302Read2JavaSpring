package app.web.pavelk.read2.repository;


import app.web.pavelk.read2.schema.Comment;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}