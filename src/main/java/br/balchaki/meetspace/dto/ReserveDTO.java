package br.balchaki.meetspace.dto;

import br.balchaki.meetspace.domain.Room.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveDTO {
    private Long reserveId;
    private Long userId;
    private Long roomId;
    private String room;
    private String user;
    private String startDate;
    private String endDate;

}
