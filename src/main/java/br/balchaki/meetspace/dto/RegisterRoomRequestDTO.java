package br.balchaki.meetspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRoomRequestDTO {
    String name;
    Integer capacity;
}
