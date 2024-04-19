package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Reservation;

import java.util.List;


public interface ReservationRepository {

  List<Reservation> findAllByUsername(String username);

  List<Reservation> getReservations();

  void delete(Reservation reservation);
}
