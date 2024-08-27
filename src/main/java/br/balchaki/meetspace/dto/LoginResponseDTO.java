package br.balchaki.meetspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private Boolean success;
    private String message;
    private String jwt;
}
