package de.hhu.propra.roommate.domain.model;

import de.hhu.propra.roommate.util.dtos.AddLockForm;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class Reservation {

  @Id
  private Long id;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final String username;
  private final Long roomId;
  private Boolean frozen;
  private final Boolean isLock;
  private AggregateReference<Seat, Long> seat;

  @PersistenceCreator
  public Reservation(Long id, LocalDateTime startDate, LocalDateTime endDate, String username,
      Long roomId, Boolean frozen, Boolean isLock) {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.username = username;
    this.roomId = roomId;
    this.frozen = frozen;
    this.isLock = isLock;
  }

  public Reservation(LocalDateTime startDate, LocalDateTime endDate, String username, Long roomId) {
    this(null, startDate, endDate, username, roomId, false, false);
  }

  public Reservation(LocalDateTime startDate, LocalDateTime endDate, String username, Long roomId,
      Boolean isLock) {
    this(null, startDate, endDate, username, roomId, false, isLock);
  }

  public static Reservation ofLock(AddLockForm addLockForm) {
    LocalDateTime startDate = addLockForm.getLockStartDate().atTime(addLockForm.getLockStartTime());
    LocalDateTime endDate = addLockForm.getLockEndDate().atTime(addLockForm.getLockEndTime());
    String username = addLockForm.getUsername();
    Long roomId = addLockForm.getRoomId();
    return new Reservation(startDate, endDate, username, roomId, true);
  }

  public String getStartDateString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd'th' HH:mm yyyy");
    return this.getStartDate().format(formatter);
  }

  public String getEndDateString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd'th' HH:mm yyyy");
    return this.getEndDate().format(formatter);
  }

  public Long getSeatId() {
    return seat == null ? null : seat.getId();
  }

  public boolean isOverlapping(Reservation that) {
    return !(this.endDate.isBefore(that.startDate) || this.startDate.isAfter(that.endDate));
  }

  public boolean isOverlapping(List<Reservation> that) {
    return that.stream().anyMatch(this::isOverlapping);
  }

  @Override
  public String toString() {
    return "Reservation{" +
        "id=" + id +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", username='" + username + '\'' +
        ", seat=" + seat +
        '}';
  }

  public void toggleFreeze() {
      if (this.frozen == null) {
          this.frozen = true;
      } else {
          this.frozen = !this.frozen;
      }
  }

}
