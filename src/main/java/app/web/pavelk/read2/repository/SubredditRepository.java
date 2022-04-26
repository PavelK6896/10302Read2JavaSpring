package app.web.pavelk.read2.repository;


import app.web.pavelk.read2.schema.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    Optional<Subreddit> findByName(String subredditName);
}
