package de.hhu.propra.roommate.adapter.web.advice;

import de.hhu.propra.roommate.util.annotations.CurrentUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class CurrentUserControllerAdvice {

  /**
   * This method is called before every request to add the current user to the model.
   *
   * @param user  The current user.
   * @param model The model.
   */
  @ModelAttribute
  public void addAttributes(
      @CurrentUser OAuth2User user,
      Model model
  ) {
    if (user != null) {
      model.addAttribute("userName", getUserName(user));
      model.addAttribute("admin", isAdmin(user));
      model.addAttribute("loggedIn", true);
    } else {
      model.addAttribute("userName", null);
      model.addAttribute("admin", false);
      model.addAttribute("loggedIn", false);
    }
  }

  /**
   * This method returns the username of the current user.
   *
   * @param principal The current user.
   * @return The username of the current user.
   */
  String getUserName(OAuth2User principal) {
    return principal != null ? principal.getAttribute("login") : null;
  }

  /**
   * This method return weather the current user is an admin.
   *
   * @param principal The current user.
   * @return True if the current user is an admin, false otherwise.
   */
  boolean isAdmin(OAuth2User principal) {
    return principal != null && principal.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

}
