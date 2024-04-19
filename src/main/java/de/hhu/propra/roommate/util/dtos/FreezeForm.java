package de.hhu.propra.roommate.util.dtos;

import lombok.Getter;


@Getter
public class FreezeForm {

  Long roomId;
  Long seatId;
  Long reservationId;

  public FreezeForm(Long roomId, Long seatId, Long reservationId) {
    this.roomId = roomId;
    this.seatId = seatId;
    this.reservationId = reservationId;
  }

}
