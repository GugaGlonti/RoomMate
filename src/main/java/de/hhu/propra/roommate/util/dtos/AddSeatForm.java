package de.hhu.propra.roommate.util.dtos;

import lombok.Getter;

import java.util.Set;

@Getter
public class AddSeatForm {

  String seatName;
  Set<String> equipment;
  Long roomId;

  public AddSeatForm(
      Set<String> equipment,
      String seatName,
      Long roomId
  ) {
    this.seatName = seatName;
    this.equipment = equipment;
    this.roomId = roomId;
  }

  public boolean hasErrors() {
    return seatName == null || seatName.isEmpty();
  }

  public String getName() {
    return seatName;
  }

  @Override
  public String toString() {
    return "AddSeatForm{" +
        "seatName='" + seatName + '\'' +
        ", equipment=" + equipment +
        ", roomID=" + roomId +
        '}';
  }

}
