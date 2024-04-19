package de.hhu.propra.roommate.domain.model;

import de.hhu.propra.roommate.util.annotations.AggregateRoot;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import java.util.HashSet;
import java.util.Set;

@Getter
@AggregateRoot
public class Room {

  @Id
  private Long id;
  private final String name;
  private final HashSet<Seat> seats;

  @PersistenceCreator
  public Room(String name, Set<Seat> seats) {
    this.name = name;
    this.seats = new HashSet<>(seats);
  }

  public Room(String name) {
    this(name, Set.of());
  }

  public Boolean hasNoSeats() {
    return seats == null || seats.isEmpty();
  }

  public Seat getSeat(Long seatId) {
    return seats.stream()
        .filter(seat -> seat.getId().equals(seatId))
        .findFirst()
        .orElseThrow();
  }

  public void addSeat(Seat seat) {
    seats.add(seat);
  }

  @Override
  public String toString() {
    return "Room{" +
        "id=" + id +
        ", name=" + name +
        ", seats=" + seats +
        '}';
  }

}
