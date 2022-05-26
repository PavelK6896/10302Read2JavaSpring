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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubReadServiceFirstImpl implements SubReadService {

    private final SubReadRepository subReadRepository;
    private final SubReadMapper subReadMapper;

    @Override
    @Transactional
    public ResponseEntity<SubReadDto> createSubRead(SubReadDto subReadDto) {
        log.debug("createSubRead");
        SubRead subRead = subReadRepository.save(subReadMapper.mapDtoToSubRead(subReadDto));
        subReadDto.setId(subRead.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subReadDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Page<SubReadDto>> getPageSubRead(Pageable pageable) {
        log.debug("getPageSubRead");
        return ResponseEntity.status(HttpStatus.OK).body(subReadRepository.findAll(pageable)
                .map(subReadMapper::mapSubReadToDto));
    }

    @Override
    public ResponseEntity<SubReadDto> getSubReadById(Long id) {
        log.debug("getSubReadById");
        SubRead subRead = subReadRepository.findById(id)
                .orElseThrow(() -> new SubReadException(ExceptionMessage.SUB_NOT_FOUND.getBodyEn().formatted(id)));
        return ResponseEntity.status(HttpStatus.OK).body(subReadMapper.mapSubReadToDto(subRead));
    }
}
