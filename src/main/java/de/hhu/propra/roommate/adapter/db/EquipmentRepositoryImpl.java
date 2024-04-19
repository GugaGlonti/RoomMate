package de.hhu.propra.roommate.adapter.db;

import de.hhu.propra.roommate.application.service.EquipmentRepository;
import de.hhu.propra.roommate.domain.model.Equipment;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public class EquipmentRepositoryImpl implements EquipmentRepository {

  private final EquipmentDAO equipmentDAO;

  public EquipmentRepositoryImpl(EquipmentDAO equipmentDAO) {
    this.equipmentDAO = equipmentDAO;
  }

  @Override
  public Set<Equipment> getEquipments() {
    return equipmentDAO.findAll();
  }

  @Override
  public void saveEquipment(Equipment equipment) {
    equipmentDAO.save(equipment);
  }

  @Override
  public void deleteEquipment(Equipment equipment) {
    equipmentDAO.delete(equipment);
  }

  @Override
  public Equipment getEquipment(String equipmentOption) {
    return equipmentDAO.findByName(equipmentOption);
  }

}
