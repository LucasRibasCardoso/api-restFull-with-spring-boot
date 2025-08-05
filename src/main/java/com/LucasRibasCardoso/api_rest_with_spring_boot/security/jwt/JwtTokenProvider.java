package com.LucasRibasCardoso.api_rest_with_spring_boot.security.jwt;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.TokenResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.security.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class JwtTokenProvider {

  private static final ZoneOffset UTC_ZONE = ZoneOffset.UTC;
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

  @Value("${security.jwt.secret-key}}")
  private String secretKey;

  @Value("${security.jwt.expiration}")
  private Long expirationMs;

  private Algorithm algorithm; // Algoritmo de assinatura do JWT

  private final UserDetailsService userDetailsService;

  public JwtTokenProvider(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    algorithm = Algorithm.HMAC256(secretKey.getBytes());
  }

  public TokenResponseDto createAccessToken(String username, List<String> roles) {
    Instant now = Instant.now();
    Instant expirationAt = now.plusSeconds(expirationMs / 1000);
    String accessToken = getAccessToken(username, roles, now, expirationAt);
    String refreshToken = getRefreshToken(username, roles, now);

    return new TokenResponseDto(
        username,
        true,
        LocalDateTime.ofInstant(now, UTC_ZONE),
        LocalDateTime.ofInstant(expirationAt, UTC_ZONE),
        accessToken,
        refreshToken);
  }

  public TokenResponseDto createRefreshToken(String refreshToken) {
    // Verifica se o token contém o prefixo "Bearer "
    if (tokenContainsBearer(refreshToken)) {
      refreshToken = refreshToken.substring("Bearer ".length());
    }

    // Decodifica o token JWT para extrair informações
    DecodedJWT decodedJWT = decodedToken(refreshToken);

    // Extrai username e roles do token decodificado
    String username = decodedJWT.getSubject();
    List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

    // Cria um novo token de acesso
    return createAccessToken(username, roles);
  }

  public Authentication getAuthentication(String token) {
    DecodedJWT decoder = decodedToken(token);
    UserDetails user = userDetailsService.loadUserByUsername(decoder.getSubject());

    return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (tokenContainsBearer(bearerToken)) {
      // retorna o token sem o prefixo "Bearer "
      return bearerToken.substring("Bearer ".length());
    }
    return null;
  }

  public boolean validateToken(String token) {
    DecodedJWT decodedJWT = decodedToken(token);
    try {
      return !decodedJWT.getExpiresAt().before(new Date());
    } catch (Exception e) {
      throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
    }
  }

  private String getAccessToken(
      String username, List<String> roles, Instant now, Instant expirationAt) {

    String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    return JWT.create()
        .withClaim("roles", roles)
        .withIssuedAt(now)
        .withExpiresAt(expirationAt)
        .withSubject(username)
        .withIssuer(issuerUrl)
        .sign(algorithm);
  }

  private String getRefreshToken(String username, List<String> roles, Instant now) {
    Instant refreshTokenExpirationAt = now.plusSeconds(604800); // 7 dias
    return JWT.create()
        .withClaim("roles", roles)
        .withIssuedAt(now)
        .withExpiresAt(refreshTokenExpirationAt)
        .withSubject(username)
        .sign(algorithm);
  }

  private DecodedJWT decodedToken(String token) {
    try {
      // Define o algoritmo de assinatura do JWT usando a chave secreta
      Algorithm algorithmDecoder = Algorithm.HMAC256(secretKey.getBytes());

      // Cria um verificador de JWT com o algoritmo de decodificação
      JWTVerifier verifier = JWT.require(algorithmDecoder).build();

      // Verifica o token JWT e retorna o token decodificado
      return verifier.verify(token);
    } catch (JWTVerificationException ex) {
      LOGGER.error("JWT verification failed: {}", ex.getMessage());
      throw new InvalidJwtAuthenticationException("Invalid JWT token");
    }
  }

  private boolean tokenContainsBearer(String bearerToken) {
    return bearerToken != null && bearerToken.startsWith("Bearer ");
  }
}
