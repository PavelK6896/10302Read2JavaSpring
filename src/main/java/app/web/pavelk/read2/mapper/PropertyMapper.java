package app.web.pavelk.read2.mapper;

import app.web.pavelk.read2.config.properties.AppProperties;
import app.web.pavelk.read2.dto.PropertyDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    PropertyDto toDto(AppProperties appProperties);

}
