package de.hhu.propra.roommate.adapter.web;

import de.hhu.propra.roommate.application.service.ReservationService;
import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.util.annotations.CurrentUser;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class UserController {

  private final ReservationService reservationService;

  public UserController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping("/{username}")
  String getUser(
      @CurrentUser OAuth2User user,
      @PathVariable String username,
      Model model,
      Boolean editing
  ) {
    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      model.addAttribute("editing", editing);
    } else {
      model.addAttribute("editing", false);
    }

    List<Reservation> reservations = reservationService.getReservationsForUser(username);
    model.addAttribute("reservations", reservations);
    model.addAttribute("username", username);
    return "user";
  }

  @GetMapping("/me")
  @ResponseBody
  Object getMe(
      OAuth2AuthenticationToken authenticationToken
  ) {
    return authenticationToken.getAuthorities().toString();
  }

}
