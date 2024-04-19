package de.hhu.propra.roommate.unit;

import de.hhu.propra.roommate.util.dtos.AddReservationForm;
import de.hhu.propra.roommate.util.dtos.SeatForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class FormTests {

  @Test
  @DisplayName("You Can Only Add A Reservation In The Future")
  void testAddForm() {
    AddReservationForm addReservationForm = new AddReservationForm(
        LocalDate.now().minusDays(1),
        LocalTime.now().minusHours(1),
        LocalDate.now().minusDays(1),
        LocalTime.now().minusHours(1),
        "username",
        1L,
        1L
    );

    assertThat(addReservationForm.getError()).isNotNull();
    assertThat(addReservationForm.getError()).isEqualTo(
        "Keine Reservierung in der Vergangenheit möglich");
  }

  @Test
  @DisplayName("Invalid Inputs Are Detected")
  void testInvalidInputs() {
    AddReservationForm addReservationForm = new AddReservationForm(
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    assertThat(addReservationForm.getError()).isNotNull();
    assertThat(addReservationForm.getError()).isEqualTo("Ungültige Eingabe");
  }

  @Test
  @DisplayName("If You Dont Specify Anything In Search Form, SeatForm Is 'Empty'")
  void testEmptySearchForm() {
    SeatForm seatForm = new SeatForm(
        null,
        null,
        null,
        null,
        null
    );

    assertThat(seatForm.hasSpecifiedEquipment()).isFalse();
    assertThat(seatForm.hasSpecifiedTime()).isFalse();
    assertThat(seatForm.isEmtpy()).isTrue();
  }

  @Test
  @DisplayName("If You Specify Equipment In Search Form, SeatForm Detects That")
  void testNotEmptyEquipmentSearchForm() {
    SeatForm seatForm = new SeatForm(
        Set.of("Random Equipment"),
        null,
        null,
        null,
        null
    );

    assertThat(seatForm.hasSpecifiedEquipment()).isTrue();
    assertThat(seatForm.hasSpecifiedTime()).isFalse();
    assertThat(seatForm.isEmtpy()).isFalse();
  }

  @Test
  @DisplayName("If You Specify Time In Search Form, SeatForm Detects That")
  void testNotEmptyTimeSearchForm() {
    SeatForm seatForm = new SeatForm(
        null,
        LocalDate.now(),
        LocalDate.now(),
        LocalTime.now(),
        LocalTime.now()
    );

    assertThat(seatForm.hasSpecifiedEquipment()).isFalse();
    assertThat(seatForm.hasSpecifiedTime()).isTrue();
    assertThat(seatForm.isEmtpy()).isFalse();
  }

}
