package app.web.pavelk.read2.repository;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.Subreddit;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.projection.PostResponseProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);

    @Query("select p from Post p ")
    Page<Post> findPage(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "subreddit"})
    @Query("select p from Post p ")
    Page<Post> findPageEntityGraphAll(Pageable pageable);

    @Modifying
    @Query("insert into Post (postName, description, user, subreddit, createdDate) " +
            "select :#{#postRequestDto.getPostName()}, " +
            ":#{#postRequestDto.getDescription()}, u, " +
            "(select s from Subreddit s where s.name = :#{#postRequestDto.getSubReadName()}), " +
            ":createdDate " +
            "from User u where u.id = :userId ")
    void insertPost(PostRequestDto postRequestDto, Long userId, LocalDateTime createdDate);

    @Query("select p.postId as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subreddit.name as subReadName, p.subreddit.id as subReadId, " +
            "(sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)) as voteCount, " +
            "count(distinct c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v on v.post.postId = p.postId " +
            "left join Vote v2 on v2.post.postId = p.postId and v2.user.id = :userId " +
            "left join Comment c on c.post.postId = p.postId " +
            "where p.postId = :postId " +
            "group by p.postId, p.postName, p.description, p.user.username, p.subreddit.name, p.subreddit.id,  p.createdDate, v2.voteType")
    PostResponseProjection findPostResponseProjectionById(Long postId, Long userId);

    @Query("select p.postId as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subreddit.name as subReadName, p.subreddit.id as subReadId, " +
            "(sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)) as voteCount, " +
            "count(distinct c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v on v.post.postId = p.postId " +
            "left join Vote v2 on v2.post.postId = p.postId and v2.user.id = :userId " +
            "left join Comment c on c.post.postId = p.postId " +
            "group by p.postId, p.postName, p.description, p.user.username, p.subreddit.name, p.subreddit.id,  p.createdDate, v2.voteType")
    Page<PostResponseProjection> findPagePost(Long userId, Pageable pageable);

    @Query("select p.postId as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subreddit.name as subReadName, p.subreddit.id as subReadId, " +
            "(sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)) as voteCount, " +
            "count(distinct c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v on v.post.postId = p.postId " +
            "left join Vote v2 on v2.post.postId = p.postId and v2.user.id = :userId " +
            "left join Comment c on c.post.postId = p.postId " +
            "where p.subreddit.id = :subredditId " +
            "group by p.postId, p.postName, p.description, p.user.username, p.subreddit.name, p.subreddit.id,  p.createdDate, v2.voteType")
    List<PostResponseProjection> findPostBySubredditId(Long subredditId, Long userId);

    @Query("select p.postId as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subreddit.name as subReadName, p.subreddit.id as subReadId, " +
            "(sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)) as voteCount, " +
            "count(distinct c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v on v.post.postId = p.postId " +
            "left join Vote v2 on v2.post.postId = p.postId and v2.user.id = :userId " +
            "left join Comment c on c.post.postId = p.postId " +
            "where p.user.username = :username " +
            "group by p.postId, p.postName, p.description, p.user.username, p.subreddit.name, p.subreddit.id,  p.createdDate, v2.voteType")
    List<PostResponseProjection> findPostByUsername(String username, Long userId);

}
