package de.hhu.propra.roommate.util.dtos;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class AddReservationForm {

  LocalDate startDate;
  LocalTime startTime;
  LocalDate endDate;
  LocalTime endTime;
  String username;
  Long seatId;
  Long roomId;

  String error = null;

  public AddReservationForm(
      LocalDate startDate,
      LocalTime startTime,
      LocalDate endDate,
      LocalTime endTime,
      String username,
      Long seatId,
      Long roomId
  ) {
    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
    this.username = username;
    this.seatId = seatId;
    this.roomId = roomId;

    if (isNotValid(this)) {
      this.error = "Ungültige Eingabe";
    } else if (!isInFuture(startDate, endDate, startTime, endTime)) {
      this.error = "Keine Reservierung in der Vergangenheit möglich";
    }
  }

  Boolean isInFuture(LocalDate startDate, LocalDate endDate, LocalTime startTime,
      LocalTime endTime) {
    LocalDateTime start = LocalDateTime.of(startDate, startTime);
    LocalDateTime end = LocalDateTime.of(endDate, endTime);
    return start.isAfter(LocalDateTime.now().minusMinutes(30)) || start.isEqual(LocalDateTime.now())
        && end.isAfter(start);
  }

  boolean isNotValid(AddReservationForm addReservationForm) {
    return addReservationForm.getStartDate() == null
        || addReservationForm.getStartTime() == null
        || addReservationForm.getEndDate() == null
        || addReservationForm.getEndTime() == null
        || addReservationForm.getUsername() == null
        || addReservationForm.getSeatId() == null
        || addReservationForm.getRoomId() == null;
  }
}
