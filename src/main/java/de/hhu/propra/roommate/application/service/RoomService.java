package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.domain.model.helpers.SearchResult;
import de.hhu.propra.roommate.util.dtos.SeatForm;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class RoomService {

  private final RoomRepository roomRepository;
  private final SeatService seatService;
  private final RoomService self;

  public RoomService(RoomRepository roomRepository, @Lazy RoomService self,
      SeatService seatService) {
    this.roomRepository = roomRepository;
    this.self = self;
    this.seatService = seatService;
  }

  public List<Room> getRooms() {
    return roomRepository.getRooms();
  }

  public List<Room> getRooms(Set<Long> roomIds) {
    return roomRepository.getRooms().stream().filter(room -> roomIds.contains(room.getId()))
        .toList();
  }

  public SearchResult getSearchResult(SeatForm seatForm) {
    SearchResult.Builder builder = new SearchResult.Builder();

    List<Seat> seats = seatService.getSeats();

    if (seatForm.hasSpecifiedEquipment()) {
      seats = seats.stream()
          .filter(seat -> seat.getEquipment().containsAll(seatForm.getEquipment())).toList();
    }

    if (seatForm.hasSpecifiedTime()) {
      Reservation desiredTimeFrame = new Reservation(seatForm.getStartDate().atStartOfDay(),
          seatForm.getEndDate().atStartOfDay(), null, null);

      seats = seats.stream().filter(seat -> !desiredTimeFrame.isOverlapping(seat.getReservations()))
          .toList();
    }

    for (Seat seat : seats) {
      builder.withSeat(seat.getRoomId(), seat.getId());
    }

    return builder.build();
  }

  public Room getRoom(Long roomID) {
    return roomRepository.getRoom(roomID);
  }

  public void saveRoom(Room room) {
    roomRepository.save(room);
  }

  public void deleteRoom(Room room) {
    roomRepository.delete(room);
  }

  public Optional<Room> getRoomByName(String roomName) {
    return roomRepository.getByName(roomName);
  }

}
