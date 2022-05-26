package app.web.pavelk.read2.service;


import app.web.pavelk.read2.dto.SubredditDto;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.SubReadException;
import app.web.pavelk.read2.mapper.SubredditMapper;
import app.web.pavelk.read2.repository.SubredditRepository;
import app.web.pavelk.read2.schema.Subreddit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubReadService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public ResponseEntity<SubredditDto> save(SubredditDto subredditDto) {
        log.debug("createSubreddit");
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<SubredditDto>> getAll() {
        log.debug("getAllSubreddits");
        return ResponseEntity.status(HttpStatus.OK).body(subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto).toList());
    }

    public ResponseEntity<SubredditDto> getSubreddit(Long id) {
        log.debug("getSubreddit");
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubReadException(ExceptionMessage.SUB_NOT_FOUND.getBodyEn().formatted(id)));
        return ResponseEntity.status(HttpStatus.OK).body(subredditMapper.mapSubredditToDto(subreddit));
    }
}
