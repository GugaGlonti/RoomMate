package de.hhu.propra.roommate.application.service;

import de.hhu.propra.roommate.domain.model.Equipment;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class EquipmentService {

  private final EquipmentRepository equipmentRepository;

  public EquipmentService(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  public Set<String> getEquipmentOptions() {
    Set<Equipment> equipments = equipmentRepository.getEquipments();
    Set<String> equipmentOptions = new HashSet<>();
    for (Equipment equipment : equipments) {
      equipmentOptions.add(equipment.getName());
    }
    return equipmentOptions;
  }

  public void addEquipment(String equipmentName) {
    Equipment equipment = new Equipment(equipmentName);
    equipmentRepository.saveEquipment(equipment);
  }

  public void deleteEquipment(Equipment equipment) {
    equipmentRepository.deleteEquipment(equipment);
  }

  public Equipment getEquipment(String equipmentOption) {
    return equipmentRepository.getEquipment(equipmentOption);
  }

}
