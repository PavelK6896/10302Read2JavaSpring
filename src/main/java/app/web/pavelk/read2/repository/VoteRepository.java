package app.web.pavelk.read2.repository;


import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.Vote;
import app.web.pavelk.read2.schema.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;


public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v.voteType from Vote v where v.post = :post and v.user = :user")
    Optional<VoteType> getTypeByUser(@Param("post") Post post, @Param("user") User user);

    @Query("select v from Vote v where v.post.id = :postId and v.user = :user")
    Optional<Vote> getTypeByUserPostId(@Param("postId") Long postId, @Param("user") User user);

    @Query("select sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)  " +
            "from Vote v where v.post = :post")
    Integer getCount(@Param("post") Post post);

    @Query("select v.post.id, " +
            "( sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end) ) " +
            "from Vote v where v.post.id in (:postIds) group by v.post.id ")
    List<Tuple> findListPostIdVoteCount(List<Long> postIds);

    @Query("select v.post.id, v.voteType " +
            "from Vote v where v.post.id in (:postIds) and v.user.id = :userId group by v.post.id, v.voteType ")
    List<Tuple> findListTuplePostIdVoteType(List<Long> postIds, Long userId);

}
