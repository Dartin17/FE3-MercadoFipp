package unoeste.fipp.mercadofipp.security;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTTokenProvider {
    private static final SecretKey CHAVE = Keys.hmacShaKeyFor(
            "MINHACHAVESECRETA_MINHACHAVESECRETA".getBytes(StandardCharsets.UTF_8));

    static public String getToken(String nome, Long id, String usuario, String nivel) {
        String jwtToken = Jwts.builder()
                .setSubject("usuario")
                .setIssuer("localhost:8080")
                .claim("nome", nome)
                .claim("id", id)
                .claim("nivel", nivel)
                .claim("usuario", usuario)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(20L)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(CHAVE)
                .compact();
        return jwtToken;
    }

    static public boolean verifyToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            if(Integer.parseInt(claims.get("nivel").toString()) == 2){
                //se entrou nesse if quer dizer que ele tem nível de guest
                // ou seja, não pode visualizar a tabela
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token).getSignature();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    static public Claims getAllClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Erro ao recuperar as informações (claims)");
        }
        return claims;
    }
}
