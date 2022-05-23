package app.web.pavelk.read2.repository;


import app.web.pavelk.read2.schema.Comment;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);

    @Query("select c.post.postId, count(c) " +
            "from Comment c where c.post.postId in (:postIds) group by c.post.postId ")
    List<Tuple> findListTuplePostIdCommentCount(List<Long> postIds);

    @Query("select new app.web.pavelk.read2.dto.CommentsDto(c.id, c.post.postId, c.createdDate, c.text, c.user.username ) " +
            "from Comment c where c.post.postId = :postId " +
            "group by c.id, c.post.postId, c.createdDate, c.text, c.user.username " +
            "order by c.createdDate desc ")
    List<app.web.pavelk.read2.dto.CommentsDto> findCommentsDtoByPostId(Long postId);

    @Query("select new app.web.pavelk.read2.dto.CommentsDto(c.id, c.post.postId, c.createdDate, c.text, c.user.username ) " +
            "from Comment c where c.user.username = :username " +
            "group by c.id, c.post.postId, c.createdDate, c.text, c.user.username " +
            "order by c.createdDate desc ")
    List<app.web.pavelk.read2.dto.CommentsDto> findCommentsDtoByUserName(String username);

}
