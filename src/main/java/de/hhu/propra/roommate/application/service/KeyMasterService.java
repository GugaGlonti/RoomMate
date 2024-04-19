package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Reservation;
import de.hhu.propra.roommate.domain.model.Room;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;


@Service
public class KeyMasterService {

  ReservationService reservationService;
  RoomService roomService;


  List<RoomKey> roomKeys = new ArrayList<>();
  List<UserKey> userKeys = new ArrayList<>();

  Logger logger = Logger.getLogger(KeyMasterService.class.getName());

  public KeyMasterService(ReservationService reservationService, RoomService roomService,
      List<RoomKey> roomKeys, List<UserKey> userKeys) {
    this.reservationService = reservationService;
    this.roomService = roomService;
    this.roomKeys = roomKeys;
    this.userKeys = userKeys;
  }


  public record Access(UUID key, UUID room) {

  }

  public record RoomKey(UUID id, String raum) {

  }

  public record RoomKeyWithId(UUID id, String raum, Long raumId) {

  }

  public record UserKey(UUID id, String owner) {

  }

  @Scheduled(cron = "0 * * * * *")
  public void fetchKeys() {
    this.roomKeys = new ArrayList<>(Objects.requireNonNull(WebClient.create().get().uri(
            uriBuilder -> uriBuilder.scheme("http").host("localhost").port(3000).path("/room").build())
        .retrieve().bodyToFlux(RoomKey.class).collectList()
        .block(Duration.of(8, ChronoUnit.SECONDS))));
    this.userKeys = new ArrayList<>(Objects.requireNonNull(WebClient.create().get().uri(
            uriBuilder -> uriBuilder.scheme("http").host("localhost").port(3000).path("/key").build())
        .retrieve().bodyToFlux(UserKey.class).collectList()
        .block(Duration.of(8, ChronoUnit.SECONDS))));
    this.logger.info("Fetched keys from KeyMaster");
  }

  public List<Access> getAccessList() {
    List<Access> accessList = new ArrayList<>();

    // Add room Ids to roomKeys
    List<RoomKeyWithId> roomKeysWithId = new ArrayList<>();
    for (RoomKey roomKey : roomKeys) {
      Optional<Room> roomOptional = roomService.getRoomByName(roomKey.raum());
      roomOptional.ifPresent(room -> roomKeysWithId.add(
          new RoomKeyWithId(roomKey.id(), roomKey.raum(), room.getId())));
    }

    // Checks if a key and room match a reservation and adds it to the accessList
    List<Reservation> reservations = reservationService.getReservations();
    for (UserKey userKey : userKeys) {
      for (RoomKeyWithId roomKey : roomKeysWithId) {
        String userName = userKey.owner();

        if (reservations.stream().anyMatch(
            reservation -> reservation.getUsername().equals(userName) && reservation.getRoomId()
                .equals(roomKey.raumId()))) {
          accessList.add(new Access(userKey.id(), roomKey.id()));
        }
      }
    }

    return accessList;
  }

}
