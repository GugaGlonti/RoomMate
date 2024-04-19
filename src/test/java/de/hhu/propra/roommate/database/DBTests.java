package de.hhu.propra.roommate.database;

import de.hhu.propra.roommate.adapter.db.*;
import de.hhu.propra.roommate.application.service.EquipmentRepository;
import de.hhu.propra.roommate.application.service.ReservationRepository;
import de.hhu.propra.roommate.application.service.RoomRepository;
import de.hhu.propra.roommate.application.service.SeatRepository;
import de.hhu.propra.roommate.domain.model.Equipment;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;


@DataJdbcTest
public class DBTests {

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

  @BeforeEach
  void setup() {
    roomRepository = new RoomRepositoryImpl(roomDAO);
    seatRepository = new SeatRepositoryImpl(seatDAO);
    reservationRepository = new ReservationRepositoryImpl(reservationDAO);
    equipmentRepository = new EquipmentRepositoryImpl(equipmentDAO);
  }

  @Test
  @DisplayName("Test if the database is empty")
  void testEmptyDatabase() {
    assertThat(seatRepository.getSeats()).isEmpty();
  }

  @Test
  @DisplayName("You can add a room to the database")
  void testAddRoom() {
    Room room = new Room("TestRoom");
    roomRepository.save(room);
    assertThat(roomRepository.getRooms()).size().isEqualTo(1);
  }

  @Test
  @DisplayName("You can delete a room from the database")
  void testDeleteRoom() {
    Room room = new Room("TestRoom");
    roomRepository.save(room);
    assertThat(roomRepository.getRooms()).size().isEqualTo(1);
    roomRepository.delete(room);
    assertThat(roomRepository.getRooms()).isEmpty();
  }

  @Test
  @DisplayName("You can add a seat to the database")
  void testAddSeat() {
    Seat seat = new Seat("TestSeat", Set.of());
    Room room = new Room("TestRoom", Set.of(seat));
    roomRepository.save(room);
    assertThat(seatRepository.getSeats()).size().isEqualTo(1);
  }

  @Test
  @DisplayName("You can delete a seat from the database")
  void testDeleteSeat() {
    Seat seat = new Seat("TestSeat", Set.of());
    Room room = new Room("TestRoom", Set.of(seat));
    roomRepository.save(room);
    seatRepository.deleteSeat(seat);
    assertThat(seatRepository.getSeats()).isEmpty();
  }

  @Test
  @DisplayName("You can add an equipment to the database")
  void testAddEquipment() {
    Equipment equipment = new Equipment("TestEquipment");
    equipmentRepository.saveEquipment(equipment);
    assertThat(equipmentRepository.getEquipments()).size().isEqualTo(1);
  }

  @Test
  @DisplayName("You can delete an equipment from the database")
  void testDeleteEquipment() {
    Equipment equipment = new Equipment("TestEquipment");
    equipmentRepository.saveEquipment(equipment);
    equipmentRepository.deleteEquipment(equipment);
    assertThat(equipmentRepository.getEquipments()).isEmpty();
  }

  /*
   * RESERVATIONS ARE TESTED IN ReservationTests.java !
   */

}
