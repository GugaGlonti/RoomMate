package de.hhu.propra.roommate.admin;

import de.hhu.propra.roommate.adapter.db.*;
import de.hhu.propra.roommate.adapter.web.AdminController;
import de.hhu.propra.roommate.application.service.*;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.util.dtos.AddSeatForm;
import de.hhu.propra.roommate.util.dtos.UpdateSeatEquipmentForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest
public class AdminTests {

  @Autowired
  RoomDAO roomDAO;
  @Autowired
  SeatDAO seatDAO;
  @Autowired
  EquipmentDAO equipmentDAO;

  RoomRepository roomRepository;
  SeatRepository seatRepository;
  EquipmentRepository equipmentRepository;

  RoomService roomService;
  SeatService seatService;
  EquipmentService equipmentService;
  AdminController adminController;

  @BeforeEach
  void setup() {
    roomRepository = new RoomRepositoryImpl(roomDAO);
    seatRepository = new SeatRepositoryImpl(seatDAO);
    equipmentRepository = new EquipmentRepositoryImpl(equipmentDAO);
    seatService = new SeatService(seatRepository, null);
    roomService = new RoomService(roomRepository, null, seatService);
    equipmentService = new EquipmentService(equipmentRepository);
    adminController = new AdminController(roomService, seatService, equipmentService);
  }

  @Test
  @DisplayName("Admin can add room")
  void adminCanAddRoom() {
    String view = adminController.addRoom("roomName", mock(RedirectAttributes.class));
    assertThat(view).isEqualTo("redirect:/rooms");
    assertThat(roomService.getRooms()).hasSize(1);
  }

  @Test
  @DisplayName("Admin can delete room")
  void adminCanDeleteRoom() {
    Room room = new Room("roomName");
    roomService.saveRoom(room);
    adminController.deleteRoom(room.getId(), mock(RedirectAttributes.class));
    assertThat(roomService.getRooms()).hasSize(0);
  }

  @Test
  @DisplayName("Admin can add seat")
  void adminCanAddSeat() {
    Room room = new Room("roomName");
    roomService.saveRoom(room);
    Room roomFromDb = roomService.getRooms().getFirst();
    assertThat(roomService.getRoom(room.getId()).getSeats()).hasSize(0);

    AddSeatForm addSeatForm = new AddSeatForm(Set.of(), "testSeat", roomFromDb.getId());
    String view = adminController.addSeat(addSeatForm, mock(RedirectAttributes.class));
    assertThat(view).isEqualTo("redirect:/rooms/" + addSeatForm.getRoomId());
    assertThat(roomService.getRoom(room.getId()).getSeats()).hasSize(1);
  }

  @Test
  @DisplayName("Admin can delete seat")
  void adminCanDeleteSeat() {
    Room room = new Room("roomName");
    roomService.saveRoom(room);
    room.addSeat(new Seat("testSeat", Set.of()));
    roomService.saveRoom(room);
    assertThat(roomService.getRoom(room.getId()).getSeats()).hasSize(1);

    adminController.deleteSeat(room.getSeats().iterator().next().getId(),
        mock(RedirectAttributes.class));
    assertThat(roomService.getRoom(room.getId()).getSeats()).hasSize(0);
  }

  @Test
  @DisplayName("Admin can add equipment")
  void adminCanAddEquipment() {
    assertThat(equipmentService.getEquipmentOptions()).hasSize(0);
    String view = adminController.addEquipment("equipmentName");
    assertThat(view).isEqualTo("redirect:/admin");
    assertThat(equipmentService.getEquipmentOptions()).hasSize(1);
  }

  @Test
  @DisplayName("Admin can delete equipment")
  void adminCanDeleteEquipment() {
    equipmentService.addEquipment("equipmentName");
    assertThat(equipmentService.getEquipmentOptions()).hasSize(1);
    String view = adminController.deleteEquipment("equipmentName");
    assertThat(view).isEqualTo("redirect:/admin");
    assertThat(equipmentService.getEquipmentOptions()).hasSize(0);
  }

  @Test
  @DisplayName("Admin can update seat equipment")
  void adminCanUpdateSeatEquipment() {
    Room room = new Room("roomName");
    roomService.saveRoom(room);
    Seat seat = new Seat("testSeat", Set.of("equipmentName1", "equipmentName2"));
    room.addSeat(seat);
    roomService.saveRoom(room);
    assertThat(
        roomService.getRoom(room.getId()).getSeats().iterator().next().getEquipment()).hasSize(2);

    UpdateSeatEquipmentForm form = new UpdateSeatEquipmentForm(1L, Set.of("equipmentName"));
    adminController.updateSeat(room.getSeats().iterator().next().getId(), form,
        mock(RedirectAttributes.class));
    assertThat(
        roomService.getRoom(room.getId()).getSeats().iterator().next().getEquipment()).hasSize(1);
  }

}
