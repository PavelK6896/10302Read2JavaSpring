package app.web.pavelk.read2.repository;

import app.web.pavelk.read2.schema.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
