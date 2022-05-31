package app.web.pavelk.read2.repository;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
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


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubRead(SubRead subRead);

    Page<Post> findPageBySubRead(SubRead subRead, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "subRead"})
    @Query("select p from Post p where p.subRead.id = :subReadId ")
    Page<Post> findAllBySubredditEntityGraphAll(Long subReadId, Pageable pageable);

    List<Post> findByUser(User user);

    Page<Post> findPageByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "subRead"})
    @Query("select p from Post p where p.user.username = :username ")
    Page<Post> findByUserEntityGraphAll(String username, Pageable pageable);

    @Query("select p from Post p ")
    Page<Post> findPage(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "subRead"})
    @Query("select p from Post p ")
    Page<Post> findPageEntityGraphAll(Pageable pageable);

    @Modifying
    @Query("insert into Post (postName, description, user, subRead, createdDate) " +
            "select :#{#postRequestDto.getPostName()}, " +
            ":#{#postRequestDto.getDescription()}, u, " +
            "(select s from SubRead s where s.name = :#{#postRequestDto.getSubReadName()}), " +
            ":createdDate " +
            "from User u where u.id = :userId ")
    void insertPost(PostRequestDto postRequestDto, Long userId, LocalDateTime createdDate);

    @Query("select p.id as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subRead.name as subReadName, p.subRead.id as subReadId, " +
            "(sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)) as voteCount, " +
            "(select count(distinct c) from Comment c where c.post.id = p.id) as commentCount, " +
            "p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v on v.post.id = p.id " +
            "left join Vote v2 on v2.post.id = p.id and v2.user.id = :userId " +
            "where p.id = :postId " +
            "group by p.id, p.postName, p.description, p.user.username, p.subRead.name, p.subRead.id,  p.createdDate, v2.voteType")
    PostResponseProjection findPostResponseProjectionById(Long postId, Long userId);

    @Query("select p.id as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subRead.name as subReadName, p.subRead.id as subReadId, " +
            "(count(distinct v0) - count(distinct v1)) as voteCount, " +
            "count(distinct c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v0 on v0.post.id = p.id and v0.voteType = 0 " +
            "left join Vote v1 on v1.post.id = p.id and v1.voteType = 1 " +
            "left join Vote v2 on v2.post.id = p.id and v2.user.id = :userId " +
            "left join Comment c on c.post.id = p.id " +
            "group by p.id, p.postName, p.description, p.user.username, p.subRead.name, p.subRead.id,  p.createdDate, v2.voteType")
    Page<PostResponseProjection> findPagePost(Long userId, Pageable pageable);

    @Query("select p.id as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subRead.name as subReadName, p.subRead.id as subReadId, " +
            "(count(distinct v0) - count(distinct v1)) as voteCount, " +
            "count(distinct c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v0 on v0.post.id = p.id and v0.voteType = 0 " +
            "left join Vote v1 on v1.post.id = p.id and v1.voteType = 1 " +
            "left join Vote v2 on v2.post.id = p.id and v2.user.id = :userId " +
            "left join Comment c on c.post.id = p.id " +
            "where p.subRead.id = :subredditId " +
            "group by p.id, p.postName, p.description, p.user.username, p.subRead.name, p.subRead.id,  p.createdDate, v2.voteType")
    Page<PostResponseProjection> findPostBySubredditId(Long subredditId, Long userId, Pageable pageable);

    @Query("select p.id as id, p.postName as postName, p.description as description, p.user.username as userName, " +
            "p.subRead.name as subReadName, p.subRead.id as subReadId, " +
            "(count(distinct v0) - count(distinct v1)) as voteCount, " +
            "count(distinct c) as commentCount, p.createdDate as duration, v2.voteType as vote " +
            "from Post p " +
            "left join Vote v0 on v0.post.id = p.id and v0.voteType = 0 " +
            "left join Vote v1 on v1.post.id = p.id and v1.voteType = 1 " +
            "left join Vote v2 on v2.post.id = p.id and v2.user.id = :userId " +
            "left join Comment c on c.post.id = p.id " +
            "where p.user.username = :username " +
            "group by p.id, p.postName, p.description, p.user.username, p.subRead.name, p.subRead.id,  p.createdDate, v2.voteType")
    Page<PostResponseProjection> findPostByUsername(String username, Long userId, Pageable pageable);

}
