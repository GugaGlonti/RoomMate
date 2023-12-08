package de.hhu.propra.roommate.web.controller;

import de.hhu.propra.roommate.annotations.AdminOnly;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class AdminController {

    @GetMapping("/seats/{seatID}/edit")
    @AdminOnly
    String GETeditSeat(
            @PathVariable("seatID") String seatID,
            Model model
    ) {
        model.addAttribute("seatID", seatID);
        return "seatEdit";
    }

}
