package de.hhu.propra.roommate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class RoomMateApplicationTests {

  @BeforeAll
  static void setup() {
    System.setProperty("spring.datasource.url", "jdbc:h2:mem:test");
    System.setProperty("spring.datasource.username",
        "this_is_not_a_real_username_something_has_to_be_here_in_order_to_work");
    System.setProperty("spring.datasource.password",
        "this_is_not_a_real_password_something_has_to_be_here_in_order_to_work");
  }

  @Test
  void contextLoads() {
  }

}
