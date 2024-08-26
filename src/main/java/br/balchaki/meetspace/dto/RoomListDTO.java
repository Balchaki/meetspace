package br.balchaki.meetspace.dto;

import br.balchaki.meetspace.domain.Room.Room;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoomListDTO {
    List<Room> rooms;

}
