package kz.meirambekuly.skidkilife.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.meirambekuly.skidkilife.web.dto.RoleDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Jwt {
    private static final String key = "MrslfeM05oNI/cvtz+SwsIDtiYunBHJBJ/z8bJczqmtyrD/gNGwJbpQ//KxhX9he9jbJ9PpWLiIjjCSz6ZWysw==";
    private static final Integer expirationDay = 7;

    public static String generateJwt(String phoneNumber, Set<RoleDto> roleDTO) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleDto role : roleDTO) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return Jwts.builder().setSubject(phoneNumber)
                .claim("authorities",
                        authorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .setIssuedAt(new Date()).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(expirationDay)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(key)))
                .compact();
    }

    public static Claims decodeJwt(String jwt) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(key))).parseClaimsJws(jwt).getBody();
    }
}
