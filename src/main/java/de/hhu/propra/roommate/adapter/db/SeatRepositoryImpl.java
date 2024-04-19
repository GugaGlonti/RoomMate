package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.application.service.SeatRepository;
import de.hhu.propra.roommate.domain.model.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SeatRepositoryImpl implements SeatRepository {

  private final SeatDAO dataSource;

  public SeatRepositoryImpl(SeatDAO dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Seat> getSeats() {
    return dataSource.findAll();
  }

  @Override
  public Seat getSeat(Long seatID) {
    return dataSource.findById(seatID).orElseThrow();
  }

  @Override
  public void deleteSeat(Seat seat) {
    dataSource.delete(seat);
  }

}
