package app.web.pavelk.read2.repository;

import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.Subreddit;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.projection.PostResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);

    @Query("select p.postId as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subreddit.name as subReadName, p.subreddit.id as subReadId, " +
            "(sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)) as voteCount, " +
            "count(c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v on v.post.postId = p.postId " +
            "left join Vote v2 on v2.post.postId = p.postId and v2.user.id = :userId " +
            "left join Comment c on c.post.postId = p.postId " +
            "group by p.postId, p.postName, p.description, p.user.username, p.subreddit.name, p.subreddit.id,  p.createdDate, v2.voteType")
    List<PostResponseProjection> findPostList(Long userId);

    @Query("select p.postId as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subreddit.name as subReadName, p.subreddit.id as subReadId, " +
            "(sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)) as voteCount, " +
            "count(c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v on v.post.postId = p.postId " +
            "left join Vote v2 on v2.post.postId = p.postId and v2.user.id = :userId " +
            "left join Comment c on c.post.postId = p.postId " +
            "where p.subreddit.id = :subredditId " +
            "group by p.postId, p.postName, p.description, p.user.username, p.subreddit.name, p.subreddit.id,  p.createdDate, v2.voteType")
    List<PostResponseProjection> findPostBySubredditId(Long subredditId, Long userId);

}
