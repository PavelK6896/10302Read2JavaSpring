package app.web.pavelk.read2.repository;


import app.web.pavelk.read2.schema.Comment;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    @EntityGraph(attributePaths = {"user", "post"})
    Slice<Comment> findByPost(Post post, Pageable pageable);

    List<Comment> findAllByUser(User user);

    @EntityGraph(attributePaths = {"user", "post"})
    Slice<Comment> findAllByUser(User user, Pageable pageable);

    @Query("select c.post.id, count(c) " +
            "from Comment c where c.post.id in (:postIds) group by c.post.id ")
    List<Tuple> findListTuplePostIdCommentCount(List<Long> postIds);

    @Query("select new app.web.pavelk.read2.dto.CommentsDto(c.id, c.post.id, c.createdDate, c.text, c.user.username ) " +
            "from Comment c where c.post.id = :postId " +
            "group by c.id, c.post.id, c.createdDate, c.text, c.user.username " +
            "order by c.createdDate desc ")
    Slice<app.web.pavelk.read2.dto.CommentsDto> findCommentsDtoByPostId(Long postId, Pageable pageable);

    @Query("select new app.web.pavelk.read2.dto.CommentsDto(c.id, c.post.id, c.createdDate, c.text, c.user.username ) " +
            "from Comment c where c.user.username = :username " +
            "group by c.id, c.post.id, c.createdDate, c.text, c.user.username " +
            "order by c.createdDate desc ")
    Slice<app.web.pavelk.read2.dto.CommentsDto> findCommentsDtoByUserName(String username, Pageable pageable);

}
