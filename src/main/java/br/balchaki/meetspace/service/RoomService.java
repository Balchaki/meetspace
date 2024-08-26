package br.balchaki.meetspace.service;

import br.balchaki.meetspace.domain.Room.Room;
import br.balchaki.meetspace.domain.Room.RoomRepository;
import br.balchaki.meetspace.dto.RoomDTO;
import br.balchaki.meetspace.dto.RoomRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllEnabled() {
        return roomRepository.findByEnabled(true);
    }

    public Boolean createRoom(Room room) {
        try {
            roomRepository.save(room);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating room: "+ e);
            return false;
        }
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Boolean toggleStatus(Integer roomId) {
        Room room = roomRepository.findById(Long.valueOf(roomId))
                .orElseThrow(()-> new EntityNotFoundException("Room not found."));
        room.setEnabled(!room.getEnabled());
        try {
            roomRepository.save(room);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving room: "+ e);
            return false;
        }
    }

    public Boolean deleteRoom(Integer id) {
        Optional<Room> optionalRoom = roomRepository.findById(id.longValue());
        if(optionalRoom.isPresent()){
            Room room = optionalRoom.get();
            roomRepository.delete(room);
            optionalRoom = roomRepository.findById(room.getRoomId());
            if(optionalRoom.isPresent()){
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

    public Boolean updateRoom(RoomDTO dto) {
        Optional<Room> optionalRoom = roomRepository.findById(dto.getRoomId().longValue());
        if(optionalRoom.isPresent()){
            Room room = optionalRoom.get();
            room.setName(dto.getName());
            room.setCapacity(dto.getCapacity());
            roomRepository.save(room);
            return true;
        }
        return false;
    }
}
