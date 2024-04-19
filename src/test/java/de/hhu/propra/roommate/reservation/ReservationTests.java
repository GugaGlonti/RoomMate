package de.hhu.propra.roommate.reservation;

import de.hhu.propra.roommate.adapter.db.*;
import de.hhu.propra.roommate.adapter.web.ReservationController;
import de.hhu.propra.roommate.application.service.*;
import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.helpers.ReservationBuilder;
import de.hhu.propra.roommate.util.dtos.AddReservationForm;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJdbcTest
public class ReservationTests {

  @Autowired
  RoomDAO roomDAO;
  @Autowired
  SeatDAO seatDAO;
  @Autowired
  ReservationDAO reservationDAO;

  RoomRepository roomRepository;
  SeatRepository seatRepository;
  ReservationRepository reservationRepository;

  SeatService seatService;
  RoomService roomService;
  ReservationService reservationService;
  ReservationController reservationController;

  @BeforeEach
  void setup() {
    roomRepository = new RoomRepositoryImpl(roomDAO);
    seatRepository = new SeatRepositoryImpl(seatDAO);
    reservationRepository = new ReservationRepositoryImpl(reservationDAO);
    seatService = new SeatService(seatRepository, null);
    roomService = new RoomService(roomRepository, null, seatService);
    reservationService = new ReservationService(reservationRepository);
    reservationController = new ReservationController(roomService);

    Room room = new Room("TestRoom");
    room.addSeat(new Seat("TestSeat", Set.of()));
    roomRepository.save(room);
  }

  @AfterEach
  void cleanUp() {
    reservationService.getReservations().forEach(reservationRepository::delete);
    seatRepository.getSeats().forEach(seatRepository::deleteSeat);
    roomRepository.getRooms().forEach(roomRepository::delete);
  }

  @Test
  @DisplayName("Test Environment is set up correctly")
  void testSetUp() {
    List<Room> testRooms = roomRepository.getRooms();
    List<Seat> testSeats = seatRepository.getSeats();
    Room testRoom = testRooms.getFirst();
    Seat testSeat = testSeats.getFirst();

    assertThat(testRooms).size().isEqualTo(1);
    assertThat(testSeats).size().isEqualTo(1);
    assertThat(testSeat.getReservations()).isEmpty();
  }

  String expectedRedirect = "redirect:/seats/1";
  String username = "TestUser";

  @Test
  @DisplayName("A reservation can be added if it is in the future")
  void testAddReservationInFuture() {
    AddReservationForm addReservationForm = new ReservationBuilder()
        .withStart(LocalDateTime.now().plusDays(1))
        .withEnd(LocalDateTime.now().plusDays(2))
        .withUsername(username)
        .buildForm();

    String view = reservationController.reserve(addReservationForm, mock(RedirectAttributes.class));
    assertThat(view).isEqualTo(expectedRedirect);

    List<Reservation> reservations = reservationService.getReservations();
    Reservation reservation = reservations.getFirst();

    assertThat(reservations).size().isEqualTo(1);
    assertThat(reservation.getUsername()).isEqualTo(username);
  }

  @Test
  @DisplayName("A reservation can not be added if it is in the past")
  void testAddReservationInPast() {
    AddReservationForm addReservationForm = new ReservationBuilder()
        .withStart(LocalDateTime.now().minusDays(1))
        .withEnd(LocalDateTime.now())
        .buildForm();

    String view = reservationController.reserve(addReservationForm, mock(RedirectAttributes.class));
    assertThat(view).isEqualTo(expectedRedirect);

    List<Reservation> reservations = reservationService.getReservations();
    assertThat(reservations).isEmpty();
  }

  @Test
  @DisplayName("A reservation cannot overlap with another reservation")
  void testAddReservationOverlap() {
    Reservation reservation = new ReservationBuilder()
        .withStart(LocalDateTime.now().plusDays(1))
        .withEnd(LocalDateTime.now().plusDays(3))
        .withUsername(username)
        .build();

    Room room = roomService.getRooms().getFirst();
    Seat seat = room.getSeats().stream().toList().getFirst();
    seat.setReservations(List.of(reservation));
    roomService.saveRoom(room);

    List<Reservation> reservations = reservationService.getReservations();
    assertThat(reservations).size().isEqualTo(1);

    AddReservationForm overlappingReservationForm = new ReservationBuilder()
        .withStart(LocalDateTime.now().plusDays(2))
        .withEnd(LocalDateTime.now().plusDays(4))
        .buildForm();

    String view = reservationController.reserve(overlappingReservationForm,
        mock(RedirectAttributes.class));
    assertThat(view).isEqualTo(expectedRedirect);

    reservations = reservationService.getReservations();
    assertThat(reservations).size().isEqualTo(1);
  }

  @Test
  @Disabled("Only works if you start this test separately, because the database is not cleaned up after each test, todo: figure this out")
  @DisplayName("Multiple reservations can be added, if they do not overlap")
  void testAddMultipleReservations() {
    Reservation reservation = new ReservationBuilder()
        .withStart(LocalDateTime.now().plusDays(1))
        .withEnd(LocalDateTime.now().plusDays(3))
        .withUsername(username)
        .build();

    Room room = roomService.getRooms().getFirst();
    Seat seat = room.getSeats().stream().toList().getFirst();
    seat.setReservations(List.of(reservation));
    roomService.saveRoom(room);

    List<Reservation> reservations = reservationService.getReservations();
    assertThat(reservations).size().isEqualTo(1);

    AddReservationForm nonOverlappingReservationForm = new ReservationBuilder()
        .withStart(LocalDateTime.now().plusMonths(1).plusDays(1))
        .withEnd(LocalDateTime.now().plusMonths(1).plusDays(2))
        .buildForm();

    String view2 = reservationController.reserve(nonOverlappingReservationForm,
        mock(RedirectAttributes.class));
    assertThat(reservationService.getReservations()).size().isEqualTo(2);

    assertThat(view2).isEqualTo(expectedRedirect);
  }

}
