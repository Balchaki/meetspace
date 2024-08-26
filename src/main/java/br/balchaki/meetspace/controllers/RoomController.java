package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.domain.Room.Room;
import br.balchaki.meetspace.dto.*;
import br.balchaki.meetspace.service.RoomService;
import br.balchaki.meetspace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> updateRoom(@RequestHeader("Authorization") String token, @RequestBody @Valid RoomDTO dto){
        if(userService.getIsAdmin(token)){
            Boolean result = roomService.updateRoom(dto);
            return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateRoomStatus(@RequestHeader("Authorization") String token, @RequestBody @Valid RoomRequestDTO dto){
        if (userService.getIsAdmin(token)) {
            Boolean result = roomService.toggleStatus(dto.getRoomId());
            return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer id, @RequestHeader("Authorization") String token){
        if(userService.getIsAdmin(token)){
            Boolean result = roomService.deleteRoom(id);
            return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRooms(@RequestHeader("Authorization") String token) {
        if (userService.getIsAdmin(token)) {
            List<Room> rooms = roomService.getAllRooms();
            return ResponseEntity.ok(new RoomListDTO(rooms));
        } else if (userService.getIsUser(token)) {
            List<Room> rooms = roomService.getAllEnabled();
            return ResponseEntity.ok(new RoomListDTO(rooms));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }




    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestHeader("Authorization") String token, @RequestBody RegisterRoomRequestDTO roomDTO) throws Exception {
        if (userService.getIsAdmin(token)) {
            Room room = new Room();
            room.setName(roomDTO.getName());
            room.setEnabled(true);
            room.setCapacity(roomDTO.getCapacity());
            Boolean success = roomService.createRoom(room);
            if(success){
                return ResponseEntity.ok().body(new SuccessResponseDTO(success));
            }else {
                return ResponseEntity.status(500).body(new SuccessResponseDTO(success));
            }
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
    }
}
