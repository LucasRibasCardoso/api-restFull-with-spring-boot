package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.LoginRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.TokenResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user.InvalidPasswordException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user.PermissionNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user.UserAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user.UserNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.UserMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Permission;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.User;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.PermissionRepository;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.UserRepository;
import com.LucasRibasCardoso.api_rest_with_spring_boot.config.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PermissionRepository permissionRepository;
  private final UserMapper userMapper;

  public AuthService(
      AuthenticationManager authenticationManager,
      JwtTokenProvider tokenProvider,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      PermissionRepository permissionRepository,
      UserMapper userMapper) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.permissionRepository = permissionRepository;
    this.userMapper = userMapper;
  }

  @Transactional
  public TokenResponseDto signIn(LoginRequestDto credentialsDto) {
    // Autentica usuário baseado nas credenciais fornecidas
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            credentialsDto.username(), credentialsDto.password()));

    // Busca usuário no banco de dados
    User user =
        userRepository
            .findByUsername(credentialsDto.username())
            .orElseThrow(
                () -> new UserNotFoundException("User not found: " + credentialsDto.username()));

    return tokenProvider.createAccessToken(user.getUsername(), user.getRoles());
  }

  public TokenResponseDto refreshToken(String username, String refreshToken) {
    if (userRepository.existsByUsername(username)) {
      return tokenProvider.createRefreshToken(refreshToken);
    }
    throw new UserNotFoundException("User not found: " + username);
  }

  @Transactional
  public SignUpResponseDto signUp(SignUpRequestDto credentialsDto) {
    // Valida as credenciais do usuário
    validateIfPasswordIsConfirmed(credentialsDto.password(), credentialsDto.confirmPassword());
    validateIfUserIsAlreadyRegistered(credentialsDto);

    // Mapeia o dto de cadastro para a entidade User
    User user = userMapper.toEntity(credentialsDto);

    String encodedPassword = passwordEncoder.encode(credentialsDto.password());

    // Define a senha codificada e as permissões padrão para o usuário
    user.setPassword(encodedPassword);
    user.setPermissions(getDefaultPermissions());

    // Salva o usuário no banco de dados
    User savedUser = userRepository.save(user);

    // Retorna o dto de resposta
    return userMapper.toDto(savedUser);
  }

  private void validateIfUserIsAlreadyRegistered(SignUpRequestDto credentialsDto) {
    if (userRepository.existsByUsername(credentialsDto.username())) {
      throw new UserAlreadyExistsException("Username is already in use.");
    }
    if (userRepository.existsByEmail(credentialsDto.email())) {
      throw new UserAlreadyExistsException("E-mail is already in use.");
    }
    if (userRepository.existsByPhone(credentialsDto.phone())) {
      throw new UserAlreadyExistsException("Phone number is already in use.");
    }
  }

  private void validateIfPasswordIsConfirmed(String password, String confirmPassword) {
    if (!password.equals(confirmPassword)) {
      throw new InvalidPasswordException("Password and Confirm Password do not match.");
    }
  }

  private List<Permission> getDefaultPermissions() {
    Permission commonUserPermission =
        permissionRepository
            .findByDescription("COMMON_USER")
            .orElseThrow(() -> new PermissionNotFoundException("Default permission not found"));
    return List.of(commonUserPermission);
  }
}
