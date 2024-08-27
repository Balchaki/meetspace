package br.balchaki.meetspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.SortedMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDatesDTO {
    private Long roomId;
    private SortedMap<LocalDate, Integer> availableDates;
}
