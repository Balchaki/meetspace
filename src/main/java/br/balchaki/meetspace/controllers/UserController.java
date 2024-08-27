package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.domain.User.User;
import br.balchaki.meetspace.dto.*;
import br.balchaki.meetspace.service.UserService;
import br.balchaki.meetspace.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO dto) {
        if(userService.getCurrentUser().getUserId() != null){
            SuccessMessageDTO response = userService.updateExistingUserPassword(dto.getPassword(), dto.getNewPassword());
            return ResponseEntity.ok().body(response);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
        if(userService.getCurrentUser().getUserId() != null){
            Boolean result = userService.updateExistingUser(dto);
            return ResponseEntity.ok().body(new SuccessResponseDTO(result));
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserData(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findByToken(token);
        if(user.getUserId() != null){
            return ResponseEntity.ok().body(new UserResponseDTO(user.getName(), user.getEmail(),user.getIsAdmin()));
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
    }
}
