package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.domain.User.User;
import br.balchaki.meetspace.dto.AuthenticationRequestDTO;
import br.balchaki.meetspace.dto.AuthenticationResponseDTO;
import br.balchaki.meetspace.dto.RegisterRequestDTO;
import br.balchaki.meetspace.dto.RegisterResponseDTO;
import br.balchaki.meetspace.security.JwtUtil;
import br.balchaki.meetspace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        try {
            if (userService.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO(false, "Usuário já registrado", null));
            }

            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(registerRequest.getPassword());

            User savedUser = userService.registerNewUser(user);

            UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new RegisterResponseDTO(true, "Usuário registrado com sucesso", jwt));
        } catch (Exception e) {
            System.err.println("Erro ao registrar usuário "+ e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegisterResponseDTO(false, "Erro ao registrar usuário", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            userService.invalidateToken(jwtToken);
            return ResponseEntity.ok().body("Logout realizado com sucesso");
        }
        return ResponseEntity.badRequest().body("Token inválido");
    }
}
