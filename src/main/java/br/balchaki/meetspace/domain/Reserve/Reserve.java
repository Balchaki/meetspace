package br.balchaki.meetspace.domain.Reserve;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "en_reservation")
@Entity(name = "Reserve")
@EqualsAndHashCode(of = "reserveId")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("reserveId")
    @Column(name = "seq_reservation")
    private Long reserveId;

    @JsonProperty("userId")
    @Column(name = "seq_user")
    private Long userId;

    @JsonProperty("roomId")
    @Column(name = "seq_room")
    private Long roomId;

    @Column(name = "date_time_start")
    private LocalDateTime startDate;

    @Column(name = "date_time_end")
    private LocalDateTime endDate;
}
