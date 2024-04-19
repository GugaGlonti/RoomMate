package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;

  public ReservationService(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  public List<Reservation> getReservations() {
    return reservationRepository.getReservations();
  }

  public List<Reservation> getReservationsForUser(String username) {
    return reservationRepository.findAllByUsername(username);
  }

}
