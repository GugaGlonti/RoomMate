package de.hhu.propra.roommate.search;

import de.hhu.propra.roommate.adapter.db.*;
import de.hhu.propra.roommate.adapter.web.AdminController;
import de.hhu.propra.roommate.application.service.*;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.domain.model.helpers.SearchResult;
import de.hhu.propra.roommate.helpers.ReservationBuilder;
import de.hhu.propra.roommate.util.dtos.SeatForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest
public class SearchTests {

  @Autowired
  RoomDAO roomDAO;
  @Autowired
  SeatDAO seatDAO;

  RoomRepository roomRepository;
  SeatRepository seatRepository;

  SeatService seatService;
  RoomService roomService;

  static Long r_1;
  static Long r_2;
  static Long s_1_1;
  static Long s_1_2;
  static Long s_2_1;
  static Long s_2_2;

  @BeforeEach
  void setup() {
    roomRepository = new RoomRepositoryImpl(roomDAO);
    seatRepository = new SeatRepositoryImpl(seatDAO);
    seatService = new SeatService(seatRepository, null);
    roomService = new RoomService(roomRepository, null, seatService);
    setUpEnvironment();
  }

  /**
   * Set up the environment for the tests 2 Rooms, 2 Seats each, 1 Reservation each first seat of
   * each room has 3 Equipments second seat has 2 Equipments: Headset, Mouse, Keyboard, HDMI
   */
  void setUpEnvironment() {
    Room room1 = new Room("TestRoom1");
    Room room2 = new Room("TestRoom2");

    Seat seat1_1 = new Seat("Seat1_1", Set.of("Headset", "Mouse", "Keyboard"));
    Seat seat1_2 = new Seat("Seat1_2", Set.of("Headset", "HDMI"));
    seat1_2.setReservations(List.of(new ReservationBuilder()
        .withStart(LocalDateTime.now().plusDays(2))
        .withEnd(LocalDateTime.now().plusDays(3))
        .withUsername("testUser")
        .build()));
    room1.addSeat(seat1_1);
    room1.addSeat(seat1_2);
    roomRepository.save(room1);

    Seat seat2_1 = new Seat("Seat2_1", Set.of("Headset", "Mouse", "Keyboard"));
    Seat seat2_2 = new Seat("Seat2_2", Set.of("Headset", "HDMI"));
    seat2_2.setReservations(List.of(new ReservationBuilder()
        .withStart(LocalDateTime.now().plusDays(5))
        .withEnd(LocalDateTime.now().plusDays(6))
        .withUsername("testUser")
        .build()));
    room2.addSeat(seat2_1);
    room2.addSeat(seat2_2);
    roomRepository.save(room2);

    List<Room> rooms = roomService.getRooms();
    for (Room room : rooms) {
        if (room.getName().equals("TestRoom1")) {
            r_1 = room.getId();
        } else {
            r_2 = room.getId();
        }
    }

    List<Seat> seats = seatService.getSeats();
    for (Seat seat : seats) {
      if (seat.getName().startsWith("Seat1")) {
          if (seat.getName().equals("Seat1_1")) {
              s_1_1 = seat.getId();
          } else {
              s_1_2 = seat.getId();
          }
      } else {
          if (seat.getName().equals("Seat2_1")) {
              s_2_1 = seat.getId();
          } else {
              s_2_2 = seat.getId();
          }
      }
    }

  }

  @Test
  @DisplayName("Seat Id's match expected seats")
  void testSeatIds() {
    assertThat(roomService.getRoom(r_1).getName()).isEqualTo("TestRoom1");
    assertThat(roomService.getRoom(r_2).getName()).isEqualTo("TestRoom2");
    assertThat(seatService.getSeat(s_1_1).getName()).isEqualTo("Seat1_1");
    assertThat(seatService.getSeat(s_1_2).getName()).isEqualTo("Seat1_2");
    assertThat(seatService.getSeat(s_2_1).getName()).isEqualTo("Seat2_1");
    assertThat(seatService.getSeat(s_2_2).getName()).isEqualTo("Seat2_2");
  }

  @Test
  @DisplayName("Test if the search result is empty")
  void testEmptySearchResult() {
    SeatForm seatForm = new SeatForm(null, null, null, null, null);
    assertThat(seatForm.isEmtpy()).isTrue();
  }

  @Test
  @DisplayName("Empty search result should return all rooms and seats")
  void testEmptySearchResultAllResults() {
    SeatForm seatForm = new SeatForm(null, null, null, null, null);

    SearchResult searchResult = roomService.getSearchResult(seatForm);
    List<Long> seatIds = getSeatIds(searchResult);

    assertThat(searchResult.getRoomIds()).containsExactlyInAnyOrder(r_1, r_2);
    assertThat(seatIds).containsExactlyInAnyOrder(s_1_1, s_1_2, s_2_1, s_2_2);
  }

  @Test
  @DisplayName("Search for Mouse should return all rooms but only the seats with Mouse")
  void testSearchForMonitor() {
    SeatForm seatForm = new SeatForm(Set.of("Mouse"), null, null, null, null);

    SearchResult searchResult = roomService.getSearchResult(seatForm);
    List<Long> seatIds = getSeatIds(searchResult);

    assertThat(searchResult.getRoomIds()).containsExactlyInAnyOrder(r_1, r_2);
    assertThat(seatIds).containsExactlyInAnyOrder(s_1_1, s_2_1);
  }

  @Test
  @DisplayName("Search for Equipment which does not exist should return no results")
  void testSearchForKeyboard() {
    SeatForm seatForm = new SeatForm(Set.of("Something"), null, null, null, null);
    SearchResult searchResult = roomService.getSearchResult(seatForm);

    assertThat(searchResult.getRoomIds()).isEmpty();
  }

  @Test
  @DisplayName("Search for free time slot should return all rooms and all seats")
  void testSearchForFreeTimeSlot() {
    SeatForm seatForm = new SeatForm(
        null,
        LocalDate.now().plusDays(7),
        LocalDate.now().plusDays(8),
        LocalTime.of(12, 0),
        LocalTime.of(12, 0));

    SearchResult searchResult = roomService.getSearchResult(seatForm);
    List<Long> seatIds = getSeatIds(searchResult);

    assertThat(searchResult.getRoomIds()).containsExactlyInAnyOrder(r_1, r_2);
    assertThat(seatIds).containsExactlyInAnyOrder(s_1_1, s_1_2, s_2_1, s_2_2);
  }

  @Test
  @DisplayName("Search overlapping 1_2 should return only 1_1, 2_1 and 2_2")
  void testSearchForReservedTimeSlot() {
    SeatForm seatForm = new SeatForm(
        null,
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(4),
        LocalTime.of(12, 0),
        LocalTime.of(12, 0));

    SearchResult searchResult = roomService.getSearchResult(seatForm);
    List<Long> seatIds = getSeatIds(searchResult);

    assertThat(searchResult.getRoomIds()).containsExactlyInAnyOrder(r_1, r_2);
    assertThat(seatIds).containsExactlyInAnyOrder(s_1_1, s_2_1, s_2_2);
  }

  @Test
  @DisplayName("Search Overlapping 1_2 and 2_2 should return only 1_1 and 2_1")
  void testSearchForReservedTimeSlot2() {
    SeatForm seatForm = new SeatForm(
        null,
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(6),
        LocalTime.of(12, 0),
        LocalTime.of(12, 0));

    SearchResult searchResult = roomService.getSearchResult(seatForm);
    List<Long> seatIds = getSeatIds(searchResult);

    assertThat(searchResult.getRoomIds()).containsExactlyInAnyOrder(r_1, r_2);
    assertThat(seatIds).containsExactlyInAnyOrder(s_1_1, s_2_1);
  }

  @Test
  @DisplayName("Headset, Hdmi but one overlapping reservation should return only 2_2")
  void testSearchForReservedTimeSlot3() {
    SeatForm seatForm = new SeatForm(
        Set.of("Headset", "HDMI"),
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(4),
        LocalTime.of(12, 0),
        LocalTime.of(12, 0));

    SearchResult searchResult = roomService.getSearchResult(seatForm);
    List<Long> seatIds = getSeatIds(searchResult);

    assertThat(searchResult.getRoomIds()).containsExactlyInAnyOrder(r_2);
    assertThat(seatIds).containsExactlyInAnyOrder(s_2_2);
  }

  @Test
  @DisplayName("Search for HDMI overlapping both reservations should return no results")
  void testSearchForReservedTimeSlot4() {
    SeatForm seatForm = new SeatForm(
        Set.of("HDMI"),
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(6),
        LocalTime.of(12, 0),
        LocalTime.of(12, 0));

    SearchResult searchResult = roomService.getSearchResult(seatForm);
    List<Long> seatIds = getSeatIds(searchResult);

    assertThat(searchResult.getRoomIds()).isEmpty();
  }

  // returns the seat ids from the search result
  private List<Long> getSeatIds(SearchResult searchResult) {
    List<Long> seatIds = new ArrayList<>();
    if (!searchResult.forRoom(r_1).isEmpty()) {
      seatIds.addAll(
          Arrays.stream(searchResult.forRoom(r_1).split(",")).map(Long::parseLong).toList());
    }
    if (!searchResult.forRoom(r_2).isEmpty()) {
      seatIds.addAll(
          Arrays.stream(searchResult.forRoom(r_2).split(",")).map(Long::parseLong).toList());
    }
    return seatIds;
  }
}
