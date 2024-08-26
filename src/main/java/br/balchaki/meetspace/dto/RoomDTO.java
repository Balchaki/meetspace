package br.balchaki.meetspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoomDTO {
    private Integer roomId;
    private String name;
    private Integer capacity;
}
