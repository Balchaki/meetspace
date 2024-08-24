package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.domain.User.User;
import br.balchaki.meetspace.dto.UserResponseDTO;
import br.balchaki.meetspace.service.UserService;
import br.balchaki.meetspace.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserData(@RequestHeader("Authorization") String token) throws Exception {
        try{
            String jwt = token.substring(7);
            if(userService.isTokenInvalid(jwt)){
                return ResponseEntity.status(401).body("Invalid token");
            }
            String email = jwtUtil.extractUsername(jwt);
            User user = userService.findByEmail(email);
            return ResponseEntity.ok().body(new UserResponseDTO(user.getName(), user.getIsAdmin()));
        } catch (Exception e) {
            throw new Exception("Error getting user data", e);
        }
    }
}
