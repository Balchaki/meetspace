package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.dto.AuthenticationRequestDTO;
import br.balchaki.meetspace.dto.AuthenticationResponseDTO;
import br.balchaki.meetspace.dto.RegisterRequestDTO;
import br.balchaki.meetspace.dto.LoginResponseDTO;
import br.balchaki.meetspace.exception.UserAlreadyExistsException;
import br.balchaki.meetspace.security.JwtUtil;
import br.balchaki.meetspace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@Valid @RequestBody AuthenticationRequestDTO authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponseDTO(true, "Authenticated  ", jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(false, "Usuário ou senha inválidos", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        try {
            String jwt = jwtUtil.generateToken(userService.registerNewUser(registerRequest));
            return ResponseEntity.ok(new LoginResponseDTO(true, "Usuário cadastrado com sucesso!", jwt));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new LoginResponseDTO(false, "Já existe uma conta vinculada a esse endereço de e-mail.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponseDTO(false, "Erro ao cadastrar usuário.", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            userService.invalidateToken(jwtToken);
            return ResponseEntity.ok("Logout successful");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}
