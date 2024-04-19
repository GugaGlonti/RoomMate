package de.hhu.propra.roommate.reservation;

import de.hhu.propra.roommate.adapter.db.*;
import de.hhu.propra.roommate.adapter.web.AdminController;
import de.hhu.propra.roommate.adapter.web.ReservationController;
import de.hhu.propra.roommate.application.service.*;
import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.helpers.ReservationBuilder;
import de.hhu.propra.roommate.security.helper.WithMockOAuth2User;
import de.hhu.propra.roommate.util.dtos.AddLockForm;
import de.hhu.propra.roommate.util.dtos.FreezeForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DataJdbcTest
public class LockAndFreezeTests {

  @MockBean
  MockMvc mockMvc;

  @Autowired
  RoomDAO roomDAO;
  @Autowired
  SeatDAO seatDAO;
  @Autowired
  ReservationDAO reservationDAO;
  @Autowired
  EquipmentDAO equipmentDAO;

  RoomRepository roomRepository;
  SeatRepository seatRepository;
  ReservationRepository reservationRepository;
  EquipmentRepository equipmentRepository;

  SeatService seatService;
  RoomService roomService;
  ReservationService reservationService;
  EquipmentService equipmentService;
  ReservationController reservationController;
  AdminController adminController;

  @BeforeEach
  void setup() {
    roomRepository = new RoomRepositoryImpl(roomDAO);
    seatRepository = new SeatRepositoryImpl(seatDAO);
    reservationRepository = new ReservationRepositoryImpl(reservationDAO);
    equipmentRepository = new EquipmentRepositoryImpl(equipmentDAO);
    seatService = new SeatService(seatRepository, null);
    roomService = new RoomService(roomRepository, null, seatService);
    reservationService = new ReservationService(reservationRepository);
    equipmentService = new EquipmentService(equipmentRepository);
    reservationController = new ReservationController(roomService);
    adminController = new AdminController(roomService, seatService, equipmentService);

    Room room = new Room("TestRoom");
    Seat seat = new Seat("TestSeat", Set.of());
    seat.setReservations(List.of(new ReservationBuilder()
        .withStart(LocalDateTime.now().plusDays(2))
        .withEnd(LocalDateTime.now().plusDays(4))
        .withUsername("testUser")
        .build()));
    room.addSeat(seat);
    roomRepository.save(room);

    Room roomFromDb = roomService.getRooms().getFirst();
    Seat seatFromDb = roomFromDb.getSeats().stream().toList().getFirst();
    Reservation reservationFromDb = seatFromDb.getReservations().stream().toList().getFirst();
    roomId = roomFromDb.getId();
    seatId = seatFromDb.getId();
    reservationId = reservationFromDb.getId();
  }

  static Long roomId;
  static Long seatId;
  static Long reservationId;

  @AfterEach
  void cleanUp() {
    reservationService.getReservations().forEach(reservationRepository::delete);
    seatRepository.getSeats().forEach(seatRepository::deleteSeat);
    roomRepository.getRooms().forEach(roomRepository::delete);
  }

  @Test
  @DisplayName("you can cancel your own reservation")
  void cancelOwnReservation() {
    OAuth2User user = mock(OAuth2User.class);
    when(user.getAttribute(anyString())).thenReturn("testUser");
    String view = reservationController.cancel(user, roomId, seatId, reservationId,
        mock(RedirectAttributes.class), false);
    assertThat(view).isEqualTo("redirect:/seats/" + seatId);
    assertThat(reservationService.getReservations()).isEmpty();
  }

  @Test
  @DisplayName("you can't cancel someone else's reservation")
  void cancelSomeoneElsesReservation() {
    OAuth2User user = mock(OAuth2User.class);
    when(user.getAttribute(anyString())).thenReturn("otherUser");
    String view = reservationController.cancel(user, roomId, seatId, reservationId,
        mock(RedirectAttributes.class), false);
    assertThat(view).isEqualTo("redirect:/seats/" + seatId);
    assertThat(reservationService.getReservations()).isNotEmpty();
  }

  @Test
  @DisplayName(("Admin can lock freeze a reservarion"))
  @WithMockOAuth2User(authorities = "ADMIN")
  void lockFreezeReservation() {
    FreezeForm freezeForm = new FreezeForm(roomId, seatId, reservationId);
    String view = adminController.freeze(freezeForm, mock(RedirectAttributes.class), false);
    assertThat(view).isEqualTo("redirect:/seats/" + seatId);
    assertThat(reservationService.getReservations().getFirst().getFrozen()).isTrue();
  }

  @Test
  @DisplayName("If a lock overlaps with a reservation, the reservation gets deleted")
  @WithMockOAuth2User(authorities = "ADMIN")
  void lockOverlapsWithReservation() {
    List<Reservation> preReservations = reservationService.getReservations();
    assertThat(preReservations).size().isEqualTo(1);
    assertThat(preReservations.getFirst().getUsername()).isEqualTo("testUser");
    AddLockForm addLockForm = new AddLockForm(
        LocalDate.now().plusDays(1),
        LocalTime.of(12, 0),
        LocalDate.now().plusDays(3),
        LocalTime.of(12, 0),
        "admin",
        seatId,
        roomId
    );
    String view = adminController.lock(addLockForm, mock(RedirectAttributes.class), false);
    assertThat(view).isEqualTo("redirect:/seats/" + seatId);
    List<Reservation> reservations = reservationService.getReservations();
    assertThat(reservations).size().isEqualTo(1);
    Reservation reservation = reservations.getFirst();
    assertThat(reservation.getIsLock()).isTrue();
    assertThat(reservation.getUsername()).isEqualTo("admin");
  }

  @Test
  @DisplayName("Locks can overlap with other locks")
  @WithMockOAuth2User(authorities = "ADMIN")
  void lockOverlapsWithLock() {
    Reservation reservation = Reservation.ofLock(new AddLockForm(
        LocalDate.now().plusDays(1),
        LocalTime.of(12, 0),
        LocalDate.now().plusDays(3),
        LocalTime.of(12, 0),
        "admin",
        seatId,
        roomId
    ));
    Room room = roomService.getRoom(roomId);
    Seat seat = room.getSeat(seatId);
    seat.setReservations(List.of(reservation));
    roomService.saveRoom(room);

    assertThat(reservationService.getReservations()).size().isEqualTo(1);

    AddLockForm addLockForm2 = new AddLockForm(
        LocalDate.now().plusDays(2),
        LocalTime.of(12, 0),
        LocalDate.now().plusDays(4),
        LocalTime.of(12, 0),
        "admin",
        seatId,
        roomId
    );
    String view = adminController.lock(addLockForm2, mock(RedirectAttributes.class), false);
    assertThat(view).isEqualTo("redirect:/seats/" + seatId);
    List<Reservation> reservations = reservationService.getReservations();
    assertThat(reservations).size().isEqualTo(2);
    assertThat(reservations.stream().filter(Reservation::getIsLock)).size().isEqualTo(2);
  }

}
