package de.hhu.propra.roommate.adapter.web;

import de.hhu.propra.roommate.application.service.EquipmentService;
import de.hhu.propra.roommate.application.service.RoomService;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.helpers.SearchResult;
import de.hhu.propra.roommate.util.annotations.CurrentUser;
import de.hhu.propra.roommate.util.dtos.SeatForm;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;


@Controller
public class RoomController {

  private final RoomService roomService;
  private final EquipmentService equipmentService;

  public RoomController(RoomService roomService, EquipmentService equipmentService) {
    this.roomService = roomService;
    this.equipmentService = equipmentService;
  }

  @GetMapping("/")
  String index(
  ) {
    return "index";
  }

  @GetMapping("/rooms")
  String getRooms(
      @CurrentUser OAuth2User user,
      SeatForm seatForm,
      Model model,
      boolean editing,
      String error
  ) {
    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      model.addAttribute("editing", editing);
    } else {
      model.addAttribute("editing", false);
    }

    SearchResult searchResult;
    List<Room> rooms;

    if (seatForm.isEmtpy()) {
      searchResult = SearchResult.empty();
      rooms = roomService.getRooms();
    } else {
      searchResult = roomService.getSearchResult(seatForm);
      rooms = roomService.getRooms(searchResult.getRoomIds());
    }

    model.addAttribute("equipmentOptions", equipmentService.getEquipmentOptions());
    model.addAttribute("seatForm", seatForm);
    model.addAttribute("searchResult", searchResult);
    model.addAttribute("rooms", rooms);
    model.addAttribute("error", error);
    return "rooms";
  }

  @GetMapping("/rooms/{roomID}")
  String getRoom(
      @CurrentUser OAuth2User user,
      @PathVariable("roomID") Long roomID,
      Model model,
      boolean editing,
      String seatIds,
      String error
  ) {
    Set<Long> highLightedSeats = SearchResult.toLongSet(seatIds);

    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      model.addAttribute("editing", editing);
    } else {
      model.addAttribute("editing", false);
    }

    Room room = roomService.getRoom(roomID);
    model.addAttribute("room", room);
    model.addAttribute("highLightedSeats", highLightedSeats);
    model.addAttribute("equipmentOptions", equipmentService.getEquipmentOptions());
    model.addAttribute("error", error);
    return "room";
  }

}
