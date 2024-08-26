package br.balchaki.meetspace.service;

import br.balchaki.meetspace.domain.Reserve.Reserve;
import br.balchaki.meetspace.domain.Reserve.ReserveRepository;
import br.balchaki.meetspace.domain.Room.Room;
import br.balchaki.meetspace.domain.Room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReserveService {
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final ReserveRepository reserveRepository;

    public ReserveService(RoomRepository roomRepository, ReserveRepository reserveRepository) {
        this.roomRepository = roomRepository;
        this.reserveRepository = reserveRepository;
    }

    public List<LocalDateTime> getAvailableTimesForRoom(Long roomId, LocalDate date) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Reserve> existingReservations = reserveRepository.findOverlappingReservations(roomId, startOfDay, endOfDay);

        List<LocalDateTime> availableTimes = new ArrayList<>();
        LocalDateTime currentTime = startOfDay;

        while (currentTime.isBefore(endOfDay)) {
            if (isTimeSlotAvailable(currentTime, existingReservations)) {
                availableTimes.add(currentTime);
            }
            currentTime = currentTime.plusHours(1); // Assuming 1-hour time slots
        }

        return availableTimes;
    }

    private boolean isTimeSlotAvailable(LocalDateTime timeSlot, List<Reserve> existingReservations) {
        for (Reserve reservation : existingReservations) {
            if ((timeSlot.isEqual(reservation.getStart_date().toLocalDateTime()) || timeSlot.isAfter(reservation.getStart_date().toLocalDateTime())) &&
                    timeSlot.isBefore(reservation.getEnd_date().toLocalDateTime())) {
                return false;
            }
        }
        return true;
    }
    public Boolean createReserve(Room room) {
        return false;
    }
}
