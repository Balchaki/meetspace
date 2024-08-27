package br.balchaki.meetspace.service;

import br.balchaki.meetspace.domain.Reserve.Reserve;
import br.balchaki.meetspace.domain.Reserve.ReserveRepository;
import br.balchaki.meetspace.domain.Room.Room;
import br.balchaki.meetspace.domain.Room.RoomRepository;
import br.balchaki.meetspace.dto.CreateReserveDTO;
import br.balchaki.meetspace.dto.ReserveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class ReserveService {
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final ReserveRepository reserveRepository;
    @Autowired
    private final UserService userService;

    public ReserveService(RoomRepository roomRepository, ReserveRepository reserveRepository, UserService userService) {
        this.roomRepository = roomRepository;
        this.reserveRepository = reserveRepository;
        this.userService = userService;
    }



    public List<LocalDateTime> getAvailableTimesForRoom(Long roomId, LocalDate date) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        LocalDateTime startOfDay = date.atTime(LocalTime.of(7, 0));
        if (LocalDate.now().equals(date) && LocalTime.now().isAfter(LocalTime.of(7, 0))) {
            startOfDay = LocalDateTime.now();
        }
        if (startOfDay.getMinute() > 30) {
            startOfDay = startOfDay.plusMinutes(60 - startOfDay.getMinute());
        } else if (startOfDay.getMinute() > 0 && startOfDay.getMinute() < 30) {
            startOfDay = startOfDay.plusMinutes(30 - startOfDay.getMinute());
        }
        LocalDateTime endOfDay = date.atTime(LocalTime.of(18, 0, 1));
        List<Reserve> existingReservations = reserveRepository.findOverlappingReservations(roomId, startOfDay, endOfDay);

        List<LocalDateTime> availableTimes = new ArrayList<>();
        LocalDateTime currentTime = startOfDay;

        while (currentTime.isBefore(endOfDay)) {
            if (isTimeSlotAvailable(currentTime, existingReservations)) {
                availableTimes.add(currentTime);
            }
            currentTime = currentTime.plusMinutes(30);
        }

        return availableTimes;
    }

    private boolean isTimeSlotAvailable(LocalDateTime timeSlot, List<Reserve> existingReservations) {
        LocalDateTime endTimeSlot = timeSlot.plusMinutes(30);
        for (Reserve reservation : existingReservations) {
            if ((timeSlot.isEqual(reservation.getStartDate()) || timeSlot.isAfter(reservation.getStartDate())) &&
                    timeSlot.isBefore(reservation.getEndDate())) {
                return false;
            }
            if (endTimeSlot.isAfter(reservation.getStartDate()) &&
                    (endTimeSlot.isBefore(reservation.getEndDate()) || endTimeSlot.isEqual(reservation.getEndDate()))) {
                return false;
            }
        }
        return true;
    }
    public Boolean createReserve(CreateReserveDTO dto) {
        Room roomEntity = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Long userId = userService.getCurrentUser().getUserId();
        LocalDateTime startDateTime = dto.getStartDate().atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneOffset.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = dto.getEndDate().atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneOffset.systemDefault()).toLocalDateTime();

        if (startDateTime.isAfter(endDateTime)) {
            throw new RuntimeException("Start date must be before end date");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Start date must be in the future");
        }

        Reserve reserve = new Reserve();
        reserve.setRoomId(dto.getRoomId());
        reserve.setUserId(userId);
        reserve.setStartDate(startDateTime);
        reserve.setEndDate(endDateTime);
        reserve = reserveRepository.save(reserve);
        return reserve.getReserveId() != null;
    }
    private boolean isTimeSlotAvailable(LocalDateTime startDateTime, LocalDateTime endDateTime, List<Reserve> existingReservations) {
        for (Reserve reservation : existingReservations) {
            if (!(endDateTime.isBefore(reservation.getStartDate()) || startDateTime.isAfter(reservation.getEndDate()))) {
                return false;
            }
        }
        return true;
    }

    public SortedMap<LocalDate, Integer> getAvailableDatesForRoom(Long roomId, LocalDate startDate) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        SortedMap<LocalDate, Integer> availableDates = new TreeMap<>();

        for (LocalDate date = startDate; date.isBefore(startDate.plusDays(30)); date = date.plusDays(1)) {
            LocalDateTime startOfDay = adjustStartTime(date);
            LocalDateTime endOfDay = date.atTime(LocalTime.of(18, 0));

            List<Reserve> existingReservations = reserveRepository.findOverlappingReservations(roomId, startOfDay, endOfDay);
            List<LocalDateTime> availableTimes = new ArrayList<>();

            LocalDateTime currentTime = startOfDay;
            while (currentTime.isBefore(endOfDay)) {
                if (isTimeSlotAvailable(currentTime, existingReservations)) {
                    availableTimes.add(currentTime);
                }
                currentTime = currentTime.plusMinutes(30);
            }

            // 0 = no available times, 1 = some available times, 2 = all available times
            // 22 time slots in a day
            if (availableTimes.size() == 22) {
                availableDates.put(date, 2);
            } else if (!availableTimes.isEmpty()) {
                availableDates.put(date, 1);
            } else {
                availableDates.put(date, 0);
            }
        }
        return availableDates;
    }

    private LocalDateTime adjustStartTime(LocalDate date) {
        LocalDateTime startOfDay = date.atTime(LocalTime.of(7, 0));
        if (LocalDate.now().equals(date) && LocalTime.now().isAfter(LocalTime.of(7, 0))) {
            startOfDay = LocalDateTime.now().withSecond(0).withNano(0);
            if (startOfDay.getMinute() > 30) {
                startOfDay = startOfDay.plusHours(1).withMinute(0);
            } else if (startOfDay.getMinute() > 0) {
                startOfDay = startOfDay.withMinute(30);
            }
        }
        return startOfDay;
    }

    public List<ReserveDTO> getReservationsForUser() {
        Long userId = userService.getCurrentUser().getUserId();
        Optional<List<Reserve>> listReserveOptional = reserveRepository.findByUserId(userId);
        if(listReserveOptional.isPresent()) {
            List<Reserve> listReserve = listReserveOptional.get();
            List<ReserveDTO> listReserveDTO = new ArrayList<>();
            for (Reserve reserve : listReserve) {
                Room room = roomRepository.findById(reserve.getRoomId())
                        .orElseThrow(() -> new RuntimeException("Room not found"));
                ReserveDTO dto = new ReserveDTO();
                dto.setReserveId(reserve.getReserveId());
                dto.setUserId(reserve.getUserId());
                dto.setRoomId(reserve.getRoomId());
                dto.setRoom(room.getName());
                dto.setUser(userService.getCurrentUser().getName()  );
                dto.setStartDate(reserve.getStartDate().toString());
                dto.setEndDate(reserve.getEndDate().toString());
                listReserveDTO.add(dto);
            }
            return listReserveDTO;
        }else{
            return new ArrayList<>();
        }
    }

    public Boolean deleteReserve(Long id) {
        Optional<Reserve> reserveOptional = reserveRepository.findById(id);
        if (reserveOptional.isPresent()) {
            Reserve reserve = reserveOptional.get();
            if (reserve.getUserId().equals(userService.getCurrentUser().getUserId()) || userService.getIsAdmin(userService.getCurrentUser().getEmail())) {
                reserveRepository.delete(reserve);
                return true;
            }
        }
        return false;
    }

    public List<ReserveDTO> getAllReservations() {
        List<Reserve> listReserve = reserveRepository.findAll();
        List<ReserveDTO> listReserveDTO = new ArrayList<>();
        for (Reserve reserve : listReserve) {
            Room room = roomRepository.findById(reserve.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            ReserveDTO dto = new ReserveDTO();
            dto.setReserveId(reserve.getReserveId());
            dto.setUserId(reserve.getUserId());
            dto.setRoomId(reserve.getRoomId());
            dto.setRoom(room.getName());
            dto.setUser(userService.findByUserId(reserve.getUserId()).getName());
            dto.setStartDate(reserve.getStartDate().toString());
            dto.setEndDate(reserve.getEndDate().toString());
            listReserveDTO.add(dto);
        }
        return listReserveDTO;
    }

}
