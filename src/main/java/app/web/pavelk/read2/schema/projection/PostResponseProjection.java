package app.web.pavelk.read2.schema.projection;

public interface PostResponseProjection {
    Long getId();

    String getPostName();

    String getDescription();

    String getUserName();

    String getSubReadName();

    Long getSubReadId();

    Integer getVoteCount();

    Integer getCommentCount();

    java.time.LocalDateTime getDuration();

    String getVote();
}
