package de.hhu.propra.roommate.adapter.web;

import de.hhu.propra.roommate.application.service.EquipmentService;
import de.hhu.propra.roommate.application.service.RoomService;
import de.hhu.propra.roommate.application.service.SeatService;
import de.hhu.propra.roommate.domain.model.Equipment;
import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import de.hhu.propra.roommate.domain.model.Seat;
import de.hhu.propra.roommate.util.annotations.AdminOnly;
import de.hhu.propra.roommate.util.dtos.AddLockForm;
import de.hhu.propra.roommate.util.dtos.AddSeatForm;
import de.hhu.propra.roommate.util.dtos.FreezeForm;
import de.hhu.propra.roommate.util.dtos.UpdateSeatEquipmentForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@AdminOnly
public class AdminController {

  private final RoomService roomService;
  private final SeatService seatService;
  private final EquipmentService equipmentService;

  public AdminController(RoomService roomService, SeatService seatService,
      EquipmentService equipmentService) {
    this.roomService = roomService;
    this.seatService = seatService;
    this.equipmentService = equipmentService;
  }

  @PostMapping("/rooms/add")
  public String addRoom(
      String roomName,
      RedirectAttributes redirectAttributes
  ) {
    try {
        if (roomName == null || roomName.isEmpty()) {
            throw new Exception("Room name must not be empty");
        }
      Room room = new Room(roomName);
      roomService.saveRoom(room);
    } catch (Exception e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    }
    redirectAttributes.addAttribute("editing", true);
    return "redirect:/rooms";
  }

  @PostMapping("/rooms/delete")
  public String deleteRoom(
      Long roomId,
      RedirectAttributes redirectAttributes
  ) {
    Room room = roomService.getRoom(roomId);
    roomService.deleteRoom(room);
    redirectAttributes.addAttribute("editing", true);
    return "redirect:/rooms";
  }

  @PostMapping("/rooms/{roomID}/addSeat")
  public String addSeat(
      AddSeatForm addSeatForm,
      RedirectAttributes redirectAttributes
  ) {
    try {
        if (addSeatForm.hasErrors()) {
            throw new Exception("Seat name must not be empty");
        }
      Room room = roomService.getRoom(addSeatForm.getRoomId());
      Seat seat = new Seat(addSeatForm.getName(), addSeatForm.getEquipment());
      room.addSeat(seat);
      roomService.saveRoom(room);
    } catch (Exception e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    }
    redirectAttributes.addAttribute("editing", true);
    return "redirect:/rooms/" + addSeatForm.getRoomId();
  }

  @PostMapping("/seats/{seatID}/delete")
  public String deleteSeat(
      @PathVariable("seatID") Long seatID,
      RedirectAttributes redirectAttributes
  ) {
    Seat seat = seatService.getSeat(seatID);
    seatService.deleteSeat(seat);
    redirectAttributes.addAttribute("editing", true);
    return "redirect:/rooms/" + seat.getRoomId();
  }

  @PostMapping("/seats/{seatID}/update")
  public String updateSeat(
      @PathVariable("seatID") Long seatID,
      UpdateSeatEquipmentForm form,
      RedirectAttributes redirectAttributes
  ) {
    System.out.println(form.getEquipment());
    Room room = roomService.getRoom(form.getRoomId());
    Seat seat = room.getSeat(seatID);
    seat.setEquipment(form.getEquipment());
    roomService.saveRoom(room);
    redirectAttributes.addAttribute("editing", true);
    return "redirect:/seats/" + seatID;
  }

  @PostMapping("/freeze")
  public String freeze(
      FreezeForm freezeForm,
      RedirectAttributes redirectAttributes,
      Boolean fromUserPage
  ) {
    Room room = roomService.getRoom(freezeForm.getRoomId());
    Seat seat = room.getSeat(freezeForm.getSeatId());
    Reservation reservation = seat.getReservation(freezeForm.getReservationId());
    reservation.toggleFreeze();
    roomService.saveRoom(room);
    redirectAttributes.addAttribute("editing", true);
      if (fromUserPage != null && fromUserPage) {
          return "redirect:/" + reservation.getUsername();
      }
    return "redirect:/seats/" + freezeForm.getSeatId();
  }

  @GetMapping("/admin")
  public String admin(
      Model model
  ) {
    model.addAttribute("equipmentOptions", equipmentService.getEquipmentOptions());
    return "admin";
  }

  @PostMapping("/equipment/add")
  public String addEquipment(
      String equipmentOption
  ) {
    equipmentService.addEquipment(equipmentOption);
    return "redirect:/admin";
  }

  @PostMapping("/equipment/delete")
  public String deleteEquipment(
      String equipmentOption
  ) {
    Equipment equipment = equipmentService.getEquipment(equipmentOption);
    equipmentService.deleteEquipment(equipment);
    return "redirect:/admin";
  }

  @PostMapping("/lock")
  public String lock(
      AddLockForm addLockForm,
      RedirectAttributes redirectAttributes,
      Boolean fromUserPage
  ) {
    Room room = roomService.getRoom(addLockForm.getRoomId());
    Seat seat = room.getSeat(addLockForm.getSeatId());
    seatService.addLockToSeat(seat, addLockForm);
    roomService.saveRoom(room);

    redirectAttributes.addAttribute("editing", true);
      if (fromUserPage != null && fromUserPage) {
          return "redirect:/" + addLockForm.getUsername();
      }
    return "redirect:/seats/" + addLockForm.getSeatId();
  }

}
