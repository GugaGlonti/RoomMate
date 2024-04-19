package de.hhu.propra.roommate.views;

import de.hhu.propra.roommate.application.service.*;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.security.helper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest
@WithMockOAuth2User
public class ViewTests {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  RoomService roomService;
  @MockBean
  SeatService seatService;
  @MockBean
  EquipmentService equipmentService;
  @MockBean
  KeyMasterService keyMasterService;
  @MockBean
  ReservationService reservationService;

  @Test
  @DisplayName("Index View")
  void indexView() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(view().name("index"));
  }

  @Test
  @DisplayName("Seats View")
  void seatsView() throws Exception {
    mockMvc.perform(get("/rooms"))
        .andExpect(view().name("rooms"));
  }

  @Test
  @DisplayName("Seat View")
  void seatView() throws Exception {
    Room room = new Room("Test Room", Set.of());
    when(roomService.getRoom(1L)).thenReturn(room);
    mockMvc.perform(get("/rooms/1"))
        .andExpect(view().name("room"));
  }

}
