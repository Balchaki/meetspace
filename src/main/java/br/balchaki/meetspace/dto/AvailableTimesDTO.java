package br.balchaki.meetspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimesDTO {
    private Long roomId;
    private List<LocalDateTime> availableTimes;
}
