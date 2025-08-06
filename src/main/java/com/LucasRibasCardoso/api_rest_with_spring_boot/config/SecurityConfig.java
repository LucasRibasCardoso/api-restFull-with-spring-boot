package com.LucasRibasCardoso.api_rest_with_spring_boot.config;

import com.LucasRibasCardoso.api_rest_with_spring_boot.config.jwt.JwtTokenFilter;
import com.LucasRibasCardoso.api_rest_with_spring_boot.config.jwt.RestAuthenticationEntryPoint;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Value("${security.password.encoder.secret}")
  private String secret;

  @Value("${security.password.encoder.iterations}")
  private int iterations;

  private final RestAuthenticationEntryPoint entryPoint;
  private final JwtTokenFilter customJwtFilter;

  public SecurityConfig(JwtTokenFilter customJwtFilter, RestAuthenticationEntryPoint entryPoint) {
    this.entryPoint = entryPoint;
    this.customJwtFilter = customJwtFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // Cria a instância customizada de Pbkdf2PasswordEncoder com os parâmetros externos
    Pbkdf2PasswordEncoder pbkdf2Encoder =
        new Pbkdf2PasswordEncoder(
            secret,
            16, // Salt length em bytes (16 é o recomendado)
            iterations,
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

    // Define o mapa de encoders
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("pbkdf2", pbkdf2Encoder);

    // Cria o DelegatingPasswordEncoder, definindo "pbkdf2" como o padrão para novos hashes
    DelegatingPasswordEncoder delegatingPasswordEncoder =
        new DelegatingPasswordEncoder("pbkdf2", encoders);

    // Define o encoder para senhas que não possuem um ID de algoritmo
    delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

    return delegatingPasswordEncoder;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {

    return configuration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        // Desabilita autenticação HTTP básica e CSRF (usando JWT no header)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)

        // Configura CORS a partir do bean corsSource
        .cors(cors -> {})

        // Não cria sessão no servidor
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // Tratamento centralizado de erros de autenticação/autorização
        .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint))

        // Regras de autorização
        .authorizeHttpRequests(
            auth ->
                auth
                    // Públicos mínimos (em PROD remover /auth/create-user ou controlar via profile)
                    .requestMatchers(
                        "/auth/signin",
                        "/auth/signup",
                        "/auth/refresh-token/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                    .permitAll()
                    // Endpoint de criação de usuário apenas em DEV (usar @Profile)
                    .requestMatchers("/auth/create-user")
                    .hasRole("ADMIN")
                    // APIs protegidas
                    .requestMatchers("/api/v1/**")
                    .authenticated()
                    // Negar tudo o mais
                    .anyRequest()
                    .denyAll())

        // Adiciona o filtro JWT antes do filtro de autenticação padrão
        .addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
