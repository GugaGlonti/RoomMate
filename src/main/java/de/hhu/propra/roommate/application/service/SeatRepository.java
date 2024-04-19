package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Seat;

import java.util.List;


public interface SeatRepository {

  List<Seat> getSeats();

  Seat getSeat(Long seatID);

  void deleteSeat(Seat seat);

}
