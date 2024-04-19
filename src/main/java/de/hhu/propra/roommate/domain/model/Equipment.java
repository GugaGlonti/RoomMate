package de.hhu.propra.roommate.domain.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;


@Getter
public class Equipment {

  @Id
  private Long id;
  private final String name;

  @PersistenceCreator
  public Equipment(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Equipment(String name) {
    this(null, name);
  }

  @Override
  public String toString() {
    return "Equipment{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
