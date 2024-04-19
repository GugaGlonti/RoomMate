package de.hhu.propra.roommate.adapter.web;

import de.hhu.propra.roommate.application.service.KeyMasterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController()
public class KeyMasterController {

  private final KeyMasterService keyMasterService;

  public KeyMasterController(KeyMasterService keyMasterService) {
    this.keyMasterService = keyMasterService;
  }


  @GetMapping("/api/access")
  public List<KeyMasterService.Access> access() {
    return keyMasterService.getAccessList();
  }

}
