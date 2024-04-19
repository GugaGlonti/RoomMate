package de.hhu.propra.roommate.adapter.web;

import de.hhu.propra.roommate.application.service.RoomService;
import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.util.annotations.CurrentUser;
import de.hhu.propra.roommate.util.dtos.AddReservationForm;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ReservationController {

  private final RoomService roomService;

  public ReservationController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("/reserve")
  public String reserve(
      AddReservationForm addReservationForm,
      RedirectAttributes redirectAttributes
  ) {
    try {
      if (addReservationForm.getError() != null) {
        throw new Exception(addReservationForm.getError());
      }
      Room room = roomService.getRoom(addReservationForm.getRoomId());
      Seat seat = room.getSeat(addReservationForm.getSeatId());
      seat.addReservation(addReservationForm);
      roomService.saveRoom(room);
    } catch (Exception e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    }
    return "redirect:/seats/" + addReservationForm.getSeatId();
  }

  @PostMapping("/cancel")
  public String cancel(
      @CurrentUser OAuth2User user,
      Long reservationId,
      Long roomId,
      Long seatId,
      RedirectAttributes redirectAttributes,
      Boolean fromUserPage
  ) {
    try {
      Room room = roomService.getRoom(roomId);
      Seat seat = room.getSeat(seatId);
      Reservation reservation = seat.getReservation(reservationId);
      seat.cancelReservation(reservation, user);
      roomService.saveRoom(room);
    } catch (Exception e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    }
    String username = user.getAttribute("login");
      if (fromUserPage != null && fromUserPage) {
          return "redirect:/" + username;
      }
    return "redirect:/seats/" + seatId;
  }

}
