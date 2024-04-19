package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.domain.model.Seat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;


public interface SeatDAO extends CrudRepository<Seat, Long> {

  @NonNull
  List<Seat> findAll();
}
