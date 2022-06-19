package app.web.pavelk.read2.security;


import app.web.pavelk.read2.config.properties.JwtProperties;
import app.web.pavelk.read2.dto.AuthenticationResponse;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.Read2Exception;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    private KeyStore keyStore;
    private final JwtProperties jwtProperties;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/key.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new Read2Exception(ExceptionMessage.INIT_JKS.getMessage(), e);
        }
    }

    public AuthenticationResponse generateToken(UserDetails userDetails) {

        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities().toString());

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeNowPlus = localDateTimeNow.plusMinutes(jwtProperties.getExpiration());

        return AuthenticationResponse.builder()
                .authenticationToken(
                        Jwts.builder()
                                .setClaims(claims)
                                .setIssuedAt(java.sql.Timestamp.valueOf(localDateTimeNow))
                                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                                .setExpiration(java.sql.Timestamp.valueOf(localDateTimeNowPlus))
                                .compact()
                )
                .expiresAt(localDateTimeNowPlus)
                .username(userDetails.getUsername()).build();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new Read2Exception(ExceptionMessage.RETRIEVING_KEY.getMessage().formatted("private"), e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new Read2Exception(ExceptionMessage.RETRIEVING_KEY.getMessage().formatted("public"), e);
        }
    }

    public String getUsernameAndValidateJwt(String token) {
        return Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
