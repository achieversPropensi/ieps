package achievers.ieps.backend.security.jwt;

import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATIONS_MS}")
    private long jwtExpirationMs;

    public String generateJwtToken(String email){
        Jwts.parser().setSigningKey(jwtSecret);
        Claims claims = Jwts.claims().setSubject(email);
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(new Date().getTime() + TimeUnit.MILLISECONDS.toMillis(jwtExpirationMs)));
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getEmailFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT Signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT Token: {}", e.getMessage());
        } catch (ExpiredJwtException e){
            logger.error("JWT Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            logger.error("JWT Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}

