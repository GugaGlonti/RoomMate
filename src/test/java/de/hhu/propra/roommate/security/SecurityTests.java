package de.hhu.propra.roommate.security;

import de.hhu.propra.roommate.application.service.*;
import de.hhu.propra.roommate.adapter.config.MethodSecurityConfig;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.security.helper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(MethodSecurityConfig.class)
@WebMvcTest
public class SecurityTests {

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
  @DisplayName("Admin Can Access Admin Routes")
  @WithMockOAuth2User(login = "AdminAndy", roles = {"ADMIN"})
  void testAdminCanAccessAdminRoutes() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("User Can't Access Admin Routes")
  @WithMockOAuth2User
  void testUserCanAccessUserRoutes() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
  }


  @Test
  @DisplayName("User Can Access /")
  @WithMockOAuth2User
  void testUserCanAccessHomepage() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("User Can Access /me")
  @WithMockOAuth2User
  void testUserCanAccessMe() throws Exception {
    mockMvc.perform(get("/me")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("User Can Access /username")
  @WithMockOAuth2User
  void testUserCanAccessUsername() throws Exception {
    mockMvc.perform(get("/username")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("User Can Access /rooms")
  @WithMockOAuth2User
  void testUserCanAccessRooms() throws Exception {
    mockMvc.perform(get("/rooms")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("User Can Access User Routes")
  @WithMockOAuth2User
  void testUserCanAccessHomepage_() throws Exception {
    Room room = mock(Room.class);
    when(room.getId()).thenReturn(1L);
    Seat seat = mock(Seat.class);
    when(seat.getId()).thenReturn(1L);
    when(roomService.getRoom(any(Long.class))).thenReturn(room);
    when(roomService.getRooms()).thenReturn(List.of(room));
    when(seatService.getSeat(any(Long.class))).thenReturn(seat);
    when(seatService.getSeats()).thenReturn(List.of(seat));

    mockMvc.perform(get("/rooms/1")).andExpect(status().isOk());
    mockMvc.perform(get("/seats")).andExpect(status().isOk());
    mockMvc.perform(get("/seats/1")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Unauthorized User Can't Access /")
  void testUnauthorizedUserCanAccessMe() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("Unauthorized User Can't Access /me")
  void testUnauthorizedUserCanAccessAdmin() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("Unauthorized User Can't Access /username")
  void testUnauthorizedUserCanAccessUsername() throws Exception {
    mockMvc.perform(get("/username")).andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("Unauthorized User Can't Access /rooms")
  void testUnauthorizedUserCanAccessRooms() throws Exception {
    mockMvc.perform(get("/rooms")).andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("Unauthorized User Can't Access /rooms/1")
  void testUnauthorizedUserCanAccessRoom() throws Exception {
    mockMvc.perform(get("/rooms/1")).andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("Unauthorized User Can't Access /seats")
  void testUnauthorizedUserCanAccessSeats() throws Exception {
    mockMvc.perform(get("/seats")).andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("Unauthorized User Can't Access /seats/1")
  void testUnauthorizedUserCanAccessSeat() throws Exception {
    mockMvc.perform(get("/seats/1")).andExpect(status().is3xxRedirection());
  }


}
