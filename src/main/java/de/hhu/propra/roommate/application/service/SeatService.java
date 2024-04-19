package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.util.dtos.AddLockForm;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SeatService {

  private final SeatRepository seatRepository;

  private final SeatService self;

  public SeatService(SeatRepository seatRepository, @Lazy SeatService self) {
    this.seatRepository = seatRepository;
    this.self = self;
  }

  public List<Seat> getSeats() {
    return seatRepository.getSeats();
  }

  public Seat getSeat(Long seatID) {
    return seatRepository.getSeat(seatID);
  }

  public void deleteSeat(Seat seat) {
    seatRepository.deleteSeat(seat);
  }

  public void addLockToSeat(Seat seat, AddLockForm addLockForm) {
    Reservation lock = Reservation.ofLock(addLockForm);
    List<Reservation> updatedReservations = new ArrayList<>(seat.getReservations().stream()
        .filter(reservation -> !reservation.isOverlapping(lock) || reservation.getIsLock())
        .toList());
    updatedReservations.add(lock);
    seat.setReservations(updatedReservations);
  }

}
