package br.balchaki.meetspace.service;

import br.balchaki.meetspace.domain.User.User;
import br.balchaki.meetspace.domain.User.UserRepository;
import br.balchaki.meetspace.dto.UserResponseDTO;
import br.balchaki.meetspace.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateExistingUserPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidatedTokens.contains(token);
    }

    public User findByToken(String token){
        try{
            String jwt = token.substring(7);
            if(isTokenInvalid(jwt)){
                throw new Exception("Autenticação com token inválido");
            }
            String email = jwtUtil.extractUsername(jwt);
            return findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair autoridades do token: " + e.getMessage(), e);
        }
    }

    public Boolean getIsAdmin(String token){
        return getAuthorities(token).contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }


    public Boolean getIsUser(String token){
        return getAuthorities(token).contains(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        final String bearerPrefix = "Bearer ";

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token não pode ser nulo ou vazio.");
        }

        if (token.startsWith(bearerPrefix)) {
            token = token.substring(bearerPrefix.length());
        }

        try {
            String email = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            return userDetails.getAuthorities();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair autoridades do token: " + e.getMessage(), e);
        }
    }
}
