package de.hhu.propra.roommate.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ControllerFirst {

    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/seats/{seatID}")
    String getSeat(
            @PathVariable("seatID") String seatID
    ) {
        return "seat";
    }

}