package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.domain.model.Equipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Set;


public interface EquipmentDAO extends CrudRepository<Equipment, Long> {

  @NonNull
  Set<Equipment> findAll();

  Equipment findByName(String name);
}
