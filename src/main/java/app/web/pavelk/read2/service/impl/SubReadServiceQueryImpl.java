package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.Read2Exception;
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

import javax.transaction.Transactional;
import java.time.Instant;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.SUB_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubReadServiceQueryImpl implements SubReadService {

    private final SubReadRepository subReadRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ResponseEntity<SubReadDto> createSubRead(SubReadDto subReadDto) {
        subReadRepository.findByName(subReadDto.getName()).ifPresent(subRead -> {
            throw new Read2Exception(SUB_EXISTS.getMessage().formatted(subRead.getName()));
        });
        SubRead subReadNew = SubRead.builder()
                .name(subReadDto.getName())
                .description(subReadDto.getDescription())
                .createdDate(Instant.now())
                .user(userService.getCurrentUserFromDB())
                .build();
        SubRead subRead = subReadRepository.save(subReadNew);
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
                .orElseThrow(() -> new Read2Exception(ExceptionMessage.SUB_NOT_FOUND.getMessage().formatted(id)));
        return ResponseEntity.status(HttpStatus.OK).body(subReadDto);
    }

    @Override
    public ResponseEntity<Page<SubReadDto>> getPageSubReadLikeStartsWith(Pageable pageable, String startsWith) {
        pageable = getDefaultPageable(pageable);
        Page<SubReadDto> subReadDtoLikeStartsWith = subReadRepository.findSubReadDtoLikeStartsWith(pageable, startsWith);
        return ResponseEntity.status(HttpStatus.OK).body(subReadDtoLikeStartsWith);
    }
}
