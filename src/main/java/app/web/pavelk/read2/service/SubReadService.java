package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.SubReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SubReadService extends CommonService {

    ResponseEntity<SubReadDto> createSubRead(SubReadDto subReadDto);

    ResponseEntity<Page<SubReadDto>> getPageSubRead(Pageable pageable);

    ResponseEntity<SubReadDto> getSubReadById(Long id);
}
