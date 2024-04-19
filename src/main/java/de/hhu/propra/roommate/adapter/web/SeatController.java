package de.hhu.propra.roommate.adapter.web;

import de.hhu.propra.roommate.application.service.EquipmentService;
import de.hhu.propra.roommate.application.service.RoomService;
import de.hhu.propra.roommate.application.service.SeatService;
import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.util.annotations.CurrentUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;


@Controller
public class SeatController {

  private final SeatService seatService;
  private final RoomService roomService;
  private final EquipmentService equipmentService;

  public SeatController(SeatService seatService, RoomService roomService,
      EquipmentService equipmentService) {
    this.seatService = seatService;
    this.roomService = roomService;
    this.equipmentService = equipmentService;
  }

  @GetMapping("/seats/{seatID}")
  String getSeat(
      @CurrentUser OAuth2User user,
      @PathVariable Long seatID,
      boolean reserving,
      boolean editing,
      Model model,
      String error
  ) {
    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      model.addAttribute("editing", editing);
    } else {
      model.addAttribute("editing", false);
    }

    Seat seat = seatService.getSeat(seatID);
    Room room = roomService.getRoom(seat.getRoomId());
    List<Reservation> reservations = seat.getReservations();
    Set<String> equipmentoptions = equipmentService.getEquipmentOptions();

    model.addAttribute("error", error);
    model.addAttribute("reserving", reserving);
    model.addAttribute("seat", seat);
    model.addAttribute("room", room);
    model.addAttribute("reservations", reservations);
    model.addAttribute("equipmentoptions", equipmentoptions);
    return "seat";
  }

  @GetMapping("/seats")
  @ResponseBody
  Object getAllSeats() {
    return seatService.getSeats();
  }

}
