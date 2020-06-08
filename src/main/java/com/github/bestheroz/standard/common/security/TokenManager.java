package com.github.bestheroz.standard.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class TokenManager {

    private final Key secretKey;

    public TokenManager(@Value("${app.token-secret-key}") final String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    /**
     * Generate a JWT with user's id as its subject
     *
     * @param userId the id of the user
     * @return a JWT value
     */
    public String jwt(final UserId userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId.value()))
                .signWith(this.secretKey).compact();
    }

    /**
     * Get user id out of a JWT value
     *
     * @param jws the jwt string
     * @return user id
     */
    public UserId verifyJwt(final String jws) {
        final String userIdValue = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(jws).getBody().getSubject();
        return new UserId(Long.valueOf(userIdValue));
    }
}
