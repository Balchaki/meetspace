package br.balchaki.meetspace.domain.Reserve;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

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
    @JsonProperty("reserveId") @Column(name = "seq_reservation")
    private Long reserveId;
    @JsonProperty("userId") @Column(name = "seq_user")
    private Long userId;
    @JsonProperty("roomId") @Column(name = "seq_room")
    private Long roomId;
    private java.sql.Timestamp start_date;
    private java.sql.Timestamp end_date;
}
