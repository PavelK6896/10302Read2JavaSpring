package app.web.pavelk.read2.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CommonService {
    default Pageable getDefaultPageable(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "createdDate"));
        }
        if (pageable.getPageSize() > 500) {
            pageable = PageRequest.of(pageable.getPageNumber(), 500, pageable.getSort());
        }
        return pageable;
    }
}
