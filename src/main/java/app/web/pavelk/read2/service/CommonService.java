package app.web.pavelk.read2.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Service common logic.
 */
public interface CommonService {
    default Pageable getDefaultPageable(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate")
                    .and(Sort.by(Sort.Direction.DESC, "id")));
        }
        if (pageable.getPageSize() > 500) {
            pageable = PageRequest.of(pageable.getPageNumber(), 500, pageable.getSort());
        }
        return pageable;
    }
}
