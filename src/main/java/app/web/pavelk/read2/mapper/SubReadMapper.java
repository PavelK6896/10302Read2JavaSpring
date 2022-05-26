package app.web.pavelk.read2.mapper;


import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubReadMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subRead.getPosts()))")
    SubReadDto mapSubReadToDto(SubRead subRead);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    SubRead mapDtoToSubRead(SubReadDto subReadDto);

}

