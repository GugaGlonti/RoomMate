package de.hhu.propra.roommate.domain.model.helpers;

import de.hhu.propra.roommate.domain.model.Seat;

import java.util.*;
import java.util.stream.Collectors;


public class SearchResult {

  Map<Long, List<Long>> room_seat;

  public SearchResult(Map<Long, ArrayList<Long>> room_seat) {
    this.room_seat = Map.copyOf(room_seat);
  }

  public static SearchResult empty() {
    return new SearchResult(Map.of());
  }

  public Boolean has(Seat seatId) {
    return room_seat.getOrDefault(seatId.getRoomId(), List.of()).contains(seatId.getId());
  }

  public Set<Long> getRoomIds() {
    return room_seat.keySet();
  }

  public String forRoom(Long roomId) {
    List<Long> seatIds = room_seat.getOrDefault(roomId, List.of());
    return this.toString(seatIds);
  }

  public static Set<Long> toLongSet(String seatIds) {
      if (seatIds == null || seatIds.isBlank()) {
          return Set.of();
      }
    return Arrays.stream(seatIds.split(",")).map(Long::parseLong).collect(Collectors.toSet());
  }

  private String toString(List<Long> seatIds) {
    return seatIds.stream().toList().stream().map(Objects::toString)
        .collect(Collectors.joining(","));
  }


  public static class Builder {

    private Map<Long, ArrayList<Long>> room_seat = new HashMap<>();

    public void withSeat(Long roomId, Long seatId) {
      ArrayList<Long> seats = room_seat.getOrDefault(roomId, new ArrayList<>());
      seats.add(seatId);
      room_seat.put(roomId, seats);
    }

    public SearchResult build() {
      return new SearchResult(this.room_seat);
    }

  }
}
