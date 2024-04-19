package de.hhu.propra.roommate.util.dtos;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
public class SeatForm {

  public Set<String> equipment;
  public LocalDate startDate;
  public LocalDate endDate;
  public LocalTime startTime;
  public LocalTime endTime;

  public SeatForm(
      Set<String> equipment,
      LocalDate startDate,
      LocalDate endDate,
      LocalTime startTime,
      LocalTime endTime
  ) {
    if (equipment == null) {
      this.equipment = Set.of();
    } else {
      this.equipment = equipment;
    }

    this.startDate = startDate;
    this.endDate = endDate;

    if (startTime == null) {
      this.startTime = LocalTime.of(0, 0);
    } else {
      this.startTime = startTime;
    }
    if (endTime == null) {
      this.endTime = LocalTime.of(23, 59);
    } else {
      this.endTime = endTime;
    }
  }

  public Boolean isEmtpy() {
    return !hasSpecifiedEquipment() && !hasSpecifiedTime();
  }

  public Boolean hasSpecifiedEquipment() {
    return !equipment.isEmpty();
  }

  public Boolean hasSpecifiedTime() {
    return startDate != null && endDate != null;
  }

  @Override
  public String toString() {
    return "SeatForm{" +
        "equipment='" + equipment + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        '}';
  }

}
