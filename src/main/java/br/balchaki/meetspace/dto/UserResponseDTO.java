package br.balchaki.meetspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserResponseDTO {
    private String name;
    private Boolean isAdmin;


}
