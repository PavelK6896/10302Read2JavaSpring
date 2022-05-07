package app.web.pavelk.read2.mapper;


import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.schema.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Named("posts")
    @Mapping(target = "id", source = "post.postId")
    @Mapping(target = "duration", source = "post.createdDate")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "subReadName", source = "post.subreddit.name")
    @Mapping(target = "subReadId", source = "post.subreddit.id")
    @Mapping(target = "voteCount", expression = "java(mapContextPosts.getPostIdVoteCountMap().get(post.getPostId()))")
    @Mapping(target = "commentCount", expression = "java(mapContextPosts.getPostIdCommentCountMap().get(post.getPostId()))")
    @Mapping(target = "vote", expression = "java(mapContextPosts.getPostIdVoteTypeMap().get(post.getPostId()))")
    PostResponseDto toDto(Post post, @Context MapContextPosts mapContextPosts);

    @IterableMapping(qualifiedByName = "posts")
    List<PostResponseDto> toDtoList(List<Post> source, @Context MapContextPosts mapContextPosts);

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class MapContextPosts {
        Map<Long, Integer> postIdVoteCountMap;
        Map<Long, Integer> postIdCommentCountMap;
        Map<Long, String> postIdVoteTypeMap;
    }
}
