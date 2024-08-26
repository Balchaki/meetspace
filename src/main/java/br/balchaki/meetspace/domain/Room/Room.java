package br.balchaki.meetspace.domain.Room;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Table(name = "en_room")
@Entity(name = "Room")
@EqualsAndHashCode(of = "roomId")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("roomId") @Column(name = "seq_room")
    private Long roomId;
    private String name;
    private Integer capacity;
    private Boolean enabled = true;
}
