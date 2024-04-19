package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.domain.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;


public interface RoomDAO extends CrudRepository<Room, Long> {

  @NonNull
  List<Room> findAll();

  Optional<Room> findByName(String roomName);
}
