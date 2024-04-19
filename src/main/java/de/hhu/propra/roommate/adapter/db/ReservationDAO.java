package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.domain.model.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;


public interface ReservationDAO extends CrudRepository<Reservation, Long> {

  @NonNull
  List<Reservation> findAll();

  List<Reservation> findAllByUsername(String username);

  void delete(Reservation reservation);
}
