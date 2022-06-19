package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.SubReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service for working with sub.
 */
public interface SubReadService extends CommonService {

    /**
     * Create new sub.
     *
     * @param subReadDto new sub data
     * @return new sub dto
     */
    ResponseEntity<SubReadDto> createSubRead(SubReadDto subReadDto);

    /**
     * Get page sub
     *
     * @param pageable page param
     * @return page subs
     */
    ResponseEntity<Page<SubReadDto>> getPageSubRead(Pageable pageable);

    /**
     * Get sub by id
     *
     * @param id sub id
     * @return sub
     */
    ResponseEntity<SubReadDto> getSubReadById(Long id);

    /**
     * Get sub by starts with name sub
     *
     * @param pageable   page param
     * @param startsWith starts with name
     * @return page subs
     */
    ResponseEntity<Page<SubReadDto>> getPageSubReadLikeStartsWith(Pageable pageable, String startsWith);
}
