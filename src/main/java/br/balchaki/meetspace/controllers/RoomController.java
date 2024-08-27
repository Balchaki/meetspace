package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.domain.Room.Room;
import br.balchaki.meetspace.dto.*;
import br.balchaki.meetspace.service.RoomService;
import br.balchaki.meetspace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms/")
public class RoomController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final RoomService roomService;

    public RoomController(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @PutMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateRoom(@RequestBody @Valid RoomDTO dto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
        if(userService.getIsAdmin(authentication.getAuthorities())){
            Boolean result = roomService.updateRoom(dto);
            return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));
        }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @PutMapping("/update-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateRoomStatus(@RequestBody @Valid RoomRequestDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
        if(userService.getIsAdmin(authentication.getAuthorities())){
            Boolean result = roomService.toggleStatus(dto.getRoomId());
            return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));
        }
       }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            if (userService.getIsAdmin(authentication.getAuthorities())) {
                Boolean result = roomService.deleteRoom(id);
                return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllRooms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            if (userService.getIsAdmin(authentication.getAuthorities())) {
                List<Room> rooms = roomService.getAllRooms();
                return ResponseEntity.ok(new RoomListDTO(rooms));
            } else if (userService.getIsUser(authentication.getAuthorities())) {
                List<Room> rooms = roomService.getAllEnabled();
                return ResponseEntity.ok(new RoomListDTO(rooms));
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }




    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createRoom(@RequestBody RegisterRoomRequestDTO roomDTO) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            if (userService.getIsAdmin(authentication.getAuthorities())) {
                Room room = new Room();
                room.setName(roomDTO.getName());
                room.setEnabled(true);
                room.setCapacity(roomDTO.getCapacity());
                Boolean success = roomService.createRoom(room);
                if (success) {
                    return ResponseEntity.ok().body(new SuccessResponseDTO(success));
                } else {
                    return ResponseEntity.status(500).body(new SuccessResponseDTO(success));
                }
            }
        }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }
}
