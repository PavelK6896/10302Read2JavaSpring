package app.web.pavelk.read2.repository;


import app.web.pavelk.read2.schema.Comment;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
