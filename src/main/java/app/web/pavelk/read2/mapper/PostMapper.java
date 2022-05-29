package app.web.pavelk.read2.mapper;


import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.projection.PostResponseProjection;
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
    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "duration", source = "post.createdDate")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "subReadName", source = "post.subRead.name")
    @Mapping(target = "subReadId", source = "post.subRead.id")
    @Mapping(target = "voteCount", expression = "java(getVoteCount(post, mapContextPosts))")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post, mapContextPosts))")
    @Mapping(target = "vote", expression = "java(mapContextPosts.getPostIdVoteTypeMap().get(post.getId()))")
    PostResponseDto toDto(Post post, @Context MapContextPosts mapContextPosts);

    @IterableMapping(qualifiedByName = "posts")
    List<PostResponseDto> toDtoList(List<Post> source, @Context MapContextPosts mapContextPosts);

    default Integer getVoteCount(Post post, MapContextPosts mapContextPosts) {
        Integer voteCount = mapContextPosts.getPostIdVoteCountMap().get(post.getId());
        return voteCount == null ? 0 : voteCount;
    }

    default Integer getCommentCount(Post post, MapContextPosts mapContextPosts) {
        Integer commentCount = mapContextPosts.getPostIdCommentCountMap().get(post.getId());
        return commentCount == null ? 0 : commentCount;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class MapContextPosts {
        Map<Long, Integer> postIdVoteCountMap;
        Map<Long, Integer> postIdCommentCountMap;
        Map<Long, String> postIdVoteTypeMap;
    }

    PostResponseDto convertProjectionToDto(PostResponseProjection post);

}
