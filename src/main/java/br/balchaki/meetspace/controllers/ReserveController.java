package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.domain.Room.Room;
import br.balchaki.meetspace.dto.AvailableTimesDTO;
import br.balchaki.meetspace.dto.RegisterRoomRequestDTO;
import br.balchaki.meetspace.dto.SuccessResponseDTO;
import br.balchaki.meetspace.service.ReserveService;
import br.balchaki.meetspace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reserve/")
public class ReserveController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final ReserveService service;

    public ReserveController(UserService userService, ReserveService service) {
        this.userService = userService;
        this.service = service;
    }

    @GetMapping("/available-times")
    public ResponseEntity<AvailableTimesDTO> getAvailableTimes(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalDateTime> availableTimes = service.getAvailableTimesForRoom(roomId, date);
        return ResponseEntity.ok(new AvailableTimesDTO(roomId, availableTimes));
    }
    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestHeader("Authorization") String token, @RequestBody RegisterRoomRequestDTO roomDTO)  {
        if (userService.getIsAdmin(token)) {
            Room room = new Room();
            room.setName(roomDTO.getName());
            room.setEnabled(true);
            room.setCapacity(roomDTO.getCapacity());
            Boolean success = service.createReserve(room);
            return ResponseEntity.status((success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(success));
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
    }
}
