package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.application.service.ReservationRepository;
import de.hhu.propra.roommate.domain.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

  private final ReservationDAO dataSource;

  public ReservationRepositoryImpl(ReservationDAO dataSource) {
    this.dataSource = dataSource;
  }

  public List<Reservation> getReservations() {
    return dataSource.findAll();
  }

  @Override
  public void delete(Reservation reservation) {
    dataSource.delete(reservation);
  }

  @Override
  public List<Reservation> findAllByUsername(String username) {
    return dataSource.findAllByUsername(username);
  }

}
