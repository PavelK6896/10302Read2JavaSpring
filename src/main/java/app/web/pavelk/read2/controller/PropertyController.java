package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.config.properties.AppProperties;
import app.web.pavelk.read2.dto.PropertyDto;
import app.web.pavelk.read2.mapper.PropertyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PropertyController {

    private final AppProperties appProperties;
    private final PropertyMapper propertyMapper;

    @GetMapping({"/property"})
    public PropertyDto getProperty() {
        return propertyMapper.toDto(appProperties);
    }
}
