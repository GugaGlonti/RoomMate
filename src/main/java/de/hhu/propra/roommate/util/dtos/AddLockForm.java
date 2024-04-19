package de.hhu.propra.roommate.util.dtos;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
public class AddLockForm {

  LocalDate lockStartDate;
  LocalTime lockStartTime;
  LocalDate lockEndDate;
  LocalTime lockEndTime;
  String username;
  Long seatId;
  Long roomId;

  public AddLockForm(
      LocalDate lockStartDate,
      LocalTime lockStartTime,
      LocalDate lockEndDate,
      LocalTime lockEndTime,
      String username,
      Long seatId,
      Long roomId
  ) {
    this.lockStartDate = lockStartDate;
    this.lockStartTime = lockStartTime;
    this.lockEndDate = lockEndDate;
    this.lockEndTime = lockEndTime;
    this.username = username;
    this.seatId = seatId;
    this.roomId = roomId;
  }

}
