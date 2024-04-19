package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Room;

import java.util.List;
import java.util.Optional;


public interface RoomRepository {

  List<Room> getRooms();

  Room getRoom(Long roomID);

  void save(Room room);

  void delete(Room room);

  Optional<Room> getByName(String roomName);
}