package de.hhu.propra.roommate.helpers;

import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.util.dtos.AddReservationForm;

import java.time.LocalDateTime;


public class ReservationBuilder {

  Long id = null;
  Long roomId = 1L;
  Long seatId = 1L;
  LocalDateTime start = LocalDateTime.now().plusDays(1);
  LocalDateTime end = LocalDateTime.now().plusDays(2);
  String username = "test_user";
  Boolean frozen = false;
  Boolean isLock = false;


  public ReservationBuilder() {
  }

  public ReservationBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ReservationBuilder withRoomId(Long roomId) {
    this.roomId = roomId;
    return this;
  }

  public ReservationBuilder withSeatId(Long seatId) {
    this.seatId = seatId;
    return this;
  }

  public ReservationBuilder withStart(LocalDateTime start) {
    this.start = start;
    return this;
  }

  public ReservationBuilder withEnd(LocalDateTime end) {
    this.end = end;
    return this;
  }

  public ReservationBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public ReservationBuilder withIsFrozen(Boolean frozen) {
    this.frozen = frozen;
    return this;
  }

  public ReservationBuilder withIsLock(Boolean isLock) {
    this.isLock = isLock;
    return this;
  }

  public Reservation build() {
    return new Reservation(id, start, end, username, roomId, isLock, isLock);
  }

  public AddReservationForm buildForm() {
    return new AddReservationForm(start.toLocalDate(), start.toLocalTime(), end.toLocalDate(),
        end.toLocalTime(), username, seatId, roomId);
  }

}
