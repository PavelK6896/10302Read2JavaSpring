package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.SubReadException;
import app.web.pavelk.read2.mapper.SubReadMapper;
import app.web.pavelk.read2.repository.SubReadRepository;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.service.SubReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubReadServiceQueryImpl implements SubReadService {

    private final SubReadRepository subReadRepository;
    private final SubReadMapper subReadMapper;

    @Override
    @Transactional
    public ResponseEntity<SubReadDto> createSubRead(SubReadDto subReadDto) {
        SubRead subRead = subReadRepository.save(subReadMapper.mapDtoToSubRead(subReadDto));
        subReadDto.setId(subRead.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subReadDto);
    }

    @Override
    public ResponseEntity<Page<SubReadDto>> getPageSubRead(Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        Page<SubReadDto> pageSubReadDto = subReadRepository.findPageSubReadDto(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageSubReadDto);
    }

    @Override
    public ResponseEntity<SubReadDto> getSubReadById(Long id) {
        SubReadDto subReadDto = subReadRepository.findSubReadDto(id)
                .orElseThrow(() -> new SubReadException(ExceptionMessage.SUB_NOT_FOUND.getBodyEn().formatted(id)));
        return ResponseEntity.status(HttpStatus.OK).body(subReadDto);
    }
}
