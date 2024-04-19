package de.hhu.propra.roommate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class RoomMateApplication {

  public static void main(String[] args) {
    SpringApplication.run(RoomMateApplication.class, args);
  }
}
