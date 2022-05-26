package app.web.pavelk.read2.repository;


import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.schema.SubRead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface SubReadRepository extends JpaRepository<SubRead, Long> {

    Optional<SubRead> findByName(String subredditName);

    @Query("select new app.web.pavelk.read2.dto.SubReadDto(s.id, s.name, s.description, s.posts.size) " +
            "from SubRead s  " +
            "group by s.id, s.name, s.description " +
            "order by s.createdDate desc ")
    Page<SubReadDto> findPageSubReadDto(Pageable pageable);

    @Query("select new app.web.pavelk.read2.dto.SubReadDto(s.id, s.name, s.description, s.posts.size) " +
            "from SubRead s where s.id = :id " +
            "group by s.id, s.name, s.description ")
    Optional<SubReadDto> findSubReadDto(Long id);

}
