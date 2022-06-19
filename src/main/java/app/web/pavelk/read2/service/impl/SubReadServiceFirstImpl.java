package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.Read2Exception;
import app.web.pavelk.read2.mapper.SubReadMapper;
import app.web.pavelk.read2.repository.SubReadRepository;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.service.SubReadService;
import app.web.pavelk.read2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.SUB_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubReadServiceFirstImpl implements SubReadService {

    private final SubReadRepository subReadRepository;
    private final SubReadMapper subReadMapper;
    private final UserService userService;

    @Override
    @Transactional
    public ResponseEntity<SubReadDto> createSubRead(SubReadDto subReadDto) {
        log.debug("createSubRead");
        subReadRepository.findByName(subReadDto.getName()).ifPresent(subRead -> {
            throw new Read2Exception(SUB_EXISTS.getMessage().formatted(subRead.getName()));
        });
        SubRead subReadNew = subReadMapper.mapDtoToSubRead(subReadDto);
        subReadNew.setCreatedDate(Instant.now());
        subReadNew.setUser(userService.getCurrentUserFromDB());
        SubRead subRead = subReadRepository.save(subReadNew);
        subReadDto.setId(subRead.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subReadDto);
    }

    @Override
    public ResponseEntity<Page<SubReadDto>> getPageSubRead(Pageable pageable) {
        pageable = getDefaultPageable(pageable);
        log.debug("getPageSubRead");
        return ResponseEntity.status(HttpStatus.OK).body(subReadRepository.findPageEntityGraph(pageable)
                .map(subReadMapper::mapSubReadToDto));
    }

    @Override
    public ResponseEntity<SubReadDto> getSubReadById(Long id) {
        log.debug("getSubReadById");
        SubRead subRead = subReadRepository.findByIdEntityGraph(id)
                .orElseThrow(() -> new Read2Exception(ExceptionMessage.SUB_NOT_FOUND.getMessage().formatted(id)));
        return ResponseEntity.status(HttpStatus.OK).body(subReadMapper.mapSubReadToDto(subRead));
    }

    @Override
    public ResponseEntity<Page<SubReadDto>> getPageSubReadLikeStartsWith(Pageable pageable, String startsWith) {
        pageable = getDefaultPageable(pageable);
        Page<SubReadDto> subReadDtoLikeStartsWith = subReadRepository.findSubReadDtoLikeStartsWith(pageable, startsWith);
        return ResponseEntity.status(HttpStatus.OK).body(subReadDtoLikeStartsWith);
    }
}
