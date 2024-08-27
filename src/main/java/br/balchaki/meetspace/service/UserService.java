package br.balchaki.meetspace.service;

import br.balchaki.meetspace.domain.User.User;
import br.balchaki.meetspace.domain.User.UserRepository;
import br.balchaki.meetspace.dto.RegisterRequestDTO;
import br.balchaki.meetspace.dto.SuccessMessageDTO;
import br.balchaki.meetspace.dto.UserDTO;
import br.balchaki.meetspace.exception.UserAlreadyExistsException;
import br.balchaki.meetspace.security.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
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

    @Transactional
    public SuccessMessageDTO updateExistingUserPassword(String oldPassword, String newPassword) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return new SuccessMessageDTO(false, "Senha antiga incorreta");
        }

        if (oldPassword.equals(newPassword)) {
            return new SuccessMessageDTO(false, "A nova senha não pode ser igual à senha antiga");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new SuccessMessageDTO(true, "Senha atualizada com sucesso");
    }

    public UserDetails registerNewUser(@Valid RegisterRequestDTO user) throws Exception {
        User newUser = new User();
        if(existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException("Email já cadastrado");
        }
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser = userRepository.save(newUser);
        if(newUser.getUserId() != null){
            return userDetailsService.loadUserByUsername(newUser.getEmail());
        }
        throw new Exception("Erro ao registrar usuário");
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

    public Boolean getIsAdmin(Collection<? extends GrantedAuthority> authorities){
        return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public Boolean getIsAdmin(String username){
        User user = findByEmail(username);
        return user.getIsAdmin();
    }


    public Boolean getIsUser(Collection<? extends GrantedAuthority> authorities){
        return authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
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

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return findByEmail(userDetails.getUsername());
        } else {
            return null;
        }
    }

    public Boolean updateExistingUser(UserDTO dto) {
        User user = getCurrentUser();
        if (Objects.nonNull(dto.getName())) {
            user.setName(dto.getName());
        }
        if (Objects.nonNull(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        user = userRepository.save(user);
        if(user.getEmail().equals(dto.getEmail()) && user.getName().equals(dto.getName())){
            return true;
        }
        return false;
    }
}
