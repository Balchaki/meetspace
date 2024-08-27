package br.balchaki.meetspace.controllers;

import br.balchaki.meetspace.domain.Room.Room;
import br.balchaki.meetspace.dto.*;
import br.balchaki.meetspace.service.ReserveService;
import br.balchaki.meetspace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAvailableTimes(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            if (date.isBefore(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            List<LocalDateTime> availableTimes = service.getAvailableTimesForRoom(roomId, date);
            return ResponseEntity.ok(new AvailableTimesDTO(roomId, availableTimes));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }


    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReserve(@RequestBody CreateReserveDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            Boolean result = service.createReserve(dto);
            return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserReserve() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            if(userService.getIsAdmin(authentication.getAuthorities()))
                return ResponseEntity.ok(service.getAllReservations());
            else
                return ResponseEntity.ok(service.getReservationsForUser());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }


    @GetMapping("/available-dates")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllRooms(@RequestParam Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            LocalDate date = LocalDate.now();
            SortedMap<LocalDate, Integer> availableDates = service.getAvailableDatesForRoom(roomId, date);
            return ResponseEntity.ok(new AvailableDatesDTO(roomId, availableDates));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteReserve(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser") {
            Boolean result = service.deleteReserve(id);
            return ResponseEntity.status((result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)).body(new SuccessResponseDTO(result));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }
}
