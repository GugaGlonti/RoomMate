package de.hhu.propra.roommate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class AdminController {

    @GetMapping("/seat/{seatID}/edit")
    String GETeditSeat(
            @PathVariable("seatID") String seatID
    ) {
        return "seatEdit";
    }

}
