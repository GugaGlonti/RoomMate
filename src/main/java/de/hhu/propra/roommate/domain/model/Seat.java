package de.hhu.propra.roommate.domain.model;

import de.hhu.propra.roommate.util.dtos.AddReservationForm;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class Seat {

  @Id
  private Long id;
  String name;
  String equipment;
  List<Reservation> reservations;
  AggregateReference<Room, Long> room;

  @PersistenceCreator
  public Seat(String name, Set<String> equipment) {
    this.name = name;
    this.reservations = List.of();

      if (equipment == null || equipment.isEmpty()) {
          this.equipment = "";
      } else {
          this.equipment = String.join(",", equipment);
      }
  }

  public Long getRoomId() {
    return room.getId();
  }

  public Set<String> getEquipment() {
      if (equipment == null || equipment.isEmpty()) {
          return Set.of();
      }
    return Arrays.stream(equipment.split(",")).collect(Collectors.toSet());
  }

  public void setEquipment(Set<String> updatedEquipment) {
      if (updatedEquipment == null || updatedEquipment.isEmpty()) {
          this.equipment = "";
      } else {
          this.equipment = String.join(",", updatedEquipment);
      }
  }

  public Boolean hasNoEquipment() {
    return equipment == null || equipment.isEmpty();
  }

  public Boolean hasNoReservations() {
    return reservations == null || reservations.isEmpty();
  }

  public Reservation getReservation(Long reservationId) {
    return reservations.stream()
        .filter(reservation -> reservation.getId().equals(reservationId))
        .findFirst()
        .orElseThrow();
  }

  public void addReservation(AddReservationForm addReservationForm) throws Exception {
    LocalDateTime startDate = LocalDateTime.of(addReservationForm.getStartDate(),
        addReservationForm.getStartTime());
    LocalDateTime endDate = LocalDateTime.of(addReservationForm.getEndDate(),
        addReservationForm.getEndTime());
    Reservation reservation = new Reservation(startDate, endDate, addReservationForm.getUsername(),
        this.getRoomId());
      if (reservation.isOverlapping(reservations)) {
          throw new Exception("Seat is already reserved at this time");
      } else {
          reservations.add(reservation);
      }
  }

  public void cancelReservation(Reservation reservation, OAuth2User user)
      throws IllegalArgumentException {
    String username = user.getAttribute("login");
    if (!reservation.getUsername().equals(username) && user.getAuthorities().stream()
        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      throw new IllegalArgumentException("You can only cancel your own reservations");
    }
    reservations.remove(reservation);
  }

  public void setReservations(List<Reservation> updatedReservations) {
    this.reservations = updatedReservations;
  }

  @Override
  public String toString() {
    return "Seat{" +
        "id=" + id +
        ", name=" + name +
        ", equipment=" + equipment +
        '}';
  }

}
