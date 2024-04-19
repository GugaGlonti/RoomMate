package de.hhu.propra.roommate.api;

import de.hhu.propra.roommate.application.service.*;
import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.helpers.ReservationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyMasterTests {

  @Test
  @DisplayName("If fetched keys are empty, the Access list should be empty")
  public void testEmptyAccessList() throws Exception {
    ReservationService reservationService = mock(ReservationService.class);
    RoomService roomService = mock(RoomService.class);
    List<KeyMasterService.RoomKey> roomKeys = List.of();
    List<KeyMasterService.UserKey> userKeys = List.of();

    KeyMasterService keyMasterService = new KeyMasterService(reservationService, roomService,
        roomKeys, userKeys);
    List<KeyMasterService.Access> accessList = keyMasterService.getAccessList();

    assertThat(accessList).isEmpty();
  }

  @Test
  @DisplayName("If roomKey, userKey and reservation match, the Access list should contain the correct Access")
  public void testCorrectAccessListWithReservation() throws Exception {
    UUID userUUID = UUID.randomUUID();
    UUID roomUUID = UUID.randomUUID();
    String username = "User 1";
    String roomName = "Room 1";
    Long roomId = 1L;
    List<KeyMasterService.RoomKey> roomKeys = List.of(
        new KeyMasterService.RoomKey(roomUUID, roomName));
    List<KeyMasterService.UserKey> userKeys = List.of(
        new KeyMasterService.UserKey(userUUID, username));

    ReservationService reservationService = mock(ReservationService.class);
    Reservation reservation = new ReservationBuilder()
        .withUsername(username)
        .withRoomId(roomId)
        .build();
    when(reservationService.getReservations()).thenReturn(List.of(reservation));
    RoomService roomService = mock(RoomService.class);
    Room room = mock(Room.class);
    when(room.getId()).thenReturn(roomId);
    when(roomService.getRooms()).thenReturn(List.of(room));
    when(roomService.getRoomByName(roomName)).thenReturn(Optional.of(room));

    KeyMasterService keyMasterService = new KeyMasterService(reservationService, roomService,
        roomKeys, userKeys);

    List<KeyMasterService.Access> accessList = keyMasterService.getAccessList();
    assertThat(accessList).isEqualTo(List.of(new KeyMasterService.Access(userUUID, roomUUID)));
  }

  @Test
  @DisplayName("If roomKey, userKey and reservations do not match match, the Access list should be empty")
  public void testCorrectAccessList() throws Exception {
    UUID userUUID = UUID.randomUUID();
    UUID roomUUID = UUID.randomUUID();
    String username = "User 1";
    String roomName = "Room 1";
    Long roomId = 1L;
    List<KeyMasterService.RoomKey> roomKeys = List.of(
        new KeyMasterService.RoomKey(roomUUID, roomName));
    List<KeyMasterService.UserKey> userKeys = List.of(
        new KeyMasterService.UserKey(userUUID, username));

    ReservationService reservationService = mock(ReservationService.class);
    Reservation reservation = new ReservationBuilder()
        .withUsername("Different User")
        .withRoomId(roomId)
        .build();
    Reservation reservation2 = new ReservationBuilder()
        .withUsername(username)
        .withRoomId(2L)
        .build();
    when(reservationService.getReservations()).thenReturn(List.of(reservation, reservation2));
    RoomService roomService = mock(RoomService.class);
    Room room = mock(Room.class);
    when(room.getId()).thenReturn(roomId);
    when(roomService.getRooms()).thenReturn(List.of(room));
    when(roomService.getRoomByName(roomName)).thenReturn(Optional.of(room));

    KeyMasterService keyMasterService = new KeyMasterService(reservationService, roomService,
        roomKeys, userKeys);

    List<KeyMasterService.Access> accessList = keyMasterService.getAccessList();
    assertThat(accessList).isEmpty();
  }

  @Test
  @DisplayName("If roomKey, userKey, and no reservation, the Access list should be empty")
  public void testReservationDoesntMatch() throws Exception {
    UUID roomUUID = UUID.randomUUID();
    UUID userUUID = UUID.randomUUID();
    List<KeyMasterService.RoomKey> roomKeys = List.of(
        new KeyMasterService.RoomKey(roomUUID, "Raum 1"));
    List<KeyMasterService.UserKey> userKeys = List.of(
        new KeyMasterService.UserKey(userUUID, "User 1"));

    ReservationService reservationService = mock(ReservationService.class);
    RoomService roomService = mock(RoomService.class);

    KeyMasterService keyMasterService = new KeyMasterService(reservationService, roomService,
        roomKeys, userKeys);

    List<KeyMasterService.Access> accessList = keyMasterService.getAccessList();
    assertThat(accessList).isEmpty();
  }

}
