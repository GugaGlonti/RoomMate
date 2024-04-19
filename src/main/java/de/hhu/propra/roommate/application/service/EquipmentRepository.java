package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Equipment;

import java.util.Set;


public interface EquipmentRepository {

  Set<Equipment> getEquipments();

  void saveEquipment(Equipment equipment);

  void deleteEquipment(Equipment equipment);

  Equipment getEquipment(String equipmentOption);

}
