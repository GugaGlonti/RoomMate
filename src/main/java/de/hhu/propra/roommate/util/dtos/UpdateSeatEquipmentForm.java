package de.hhu.propra.roommate.util.dtos;

import lombok.Getter;

import java.util.Set;

@Getter
public class UpdateSeatEquipmentForm {

  Long roomId;
  Set<String> equipment;

  public UpdateSeatEquipmentForm(Long roomId, Set<String> equipment) {
    this.roomId = roomId;
    this.equipment = equipment;
  }

  @Override
  public String toString() {
    return "UpdateSeatEquipmentForm{" +
        "roomId=" + roomId +
        ", equipment=" + equipment +
        '}';
  }

}
