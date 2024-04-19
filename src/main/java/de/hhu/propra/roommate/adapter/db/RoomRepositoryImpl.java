package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.application.service.RoomRepository;
import de.hhu.propra.roommate.domain.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class RoomRepositoryImpl implements RoomRepository {

  private final RoomDAO dataSource;

  public RoomRepositoryImpl(RoomDAO dataSource) {
    this.dataSource = dataSource;
  }

  public List<Room> getRooms() {
    return dataSource.findAll();
  }

  @Override
  public Room getRoom(Long roomID) {
    return dataSource.findById(roomID)
        .orElseThrow(() -> new IllegalArgumentException("Room not found"));
  }

  @Override
  public void save(Room room) {
    dataSource.save(room);
  }

  @Override
  public void delete(Room room) {
    dataSource.delete(room);
  }

  @Override
  public Optional<Room> getByName(String roomName) {
    return dataSource.findByName(roomName);
  }

}
