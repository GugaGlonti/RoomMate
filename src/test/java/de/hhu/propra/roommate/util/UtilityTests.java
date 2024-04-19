package de.hhu.propra.roommate.util;

import de.hhu.propra.roommate.adapter.web.advice.CurrentUserControllerAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;


public class UtilityTests {

  @Test
  @DisplayName("Current user data is in every view")
  public void testCurrentUserAdvice() {
    OAuth2User user = mock(OAuth2User.class);
    when(user.getAttribute("login")).thenReturn("test");
    when(user.getAuthorities()).thenReturn(List.of());

    Model model = fakeModel;

    CurrentUserControllerAdvice advice = new CurrentUserControllerAdvice();
    advice.addAttributes(user, model);

    assertThat(model.asMap().get("userName")).isEqualTo("test");
    assertThat(model.asMap().get("admin")).isEqualTo(false);
    assertThat(model.asMap().get("loggedIn")).isEqualTo(true);
  }

  @Test
  @DisplayName("If current user is Admin, admin attribute is true")
  public void testCurrentUserAdviceAdmin() {
    OAuth2User user = mock(OAuth2User.class);
    when(user.getAttribute("login")).thenReturn("test");
    List authoritiesList = new ArrayList();
    GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
    authoritiesList.add(adminAuthority);
    when(user.getAuthorities()).thenReturn(authoritiesList);

    Model model = fakeModel;

    CurrentUserControllerAdvice advice = new CurrentUserControllerAdvice();
    advice.addAttributes(user, model);

    assertThat(model.asMap().get("userName")).isEqualTo("test");
    assertThat(model.asMap().get("admin")).isEqualTo(true);
    assertThat(model.asMap().get("loggedIn")).isEqualTo(true);
  }

  @Test
  @DisplayName("If current user is null, the attributes are null or false")
  public void testCurrentUserAdviceNull() {
    OAuth2User user = null;

    Model model = fakeModel;

    CurrentUserControllerAdvice advice = new CurrentUserControllerAdvice();
    advice.addAttributes(user, model);

    assertThat(model.asMap().get("userName")).isEqualTo(null);
    assertThat(model.asMap().get("admin")).isEqualTo(false);
    assertThat(model.asMap().get("loggedIn")).isEqualTo(false);
  }


  Model fakeModel = new Model() {
    private final Map<String, Object> attributes = new HashMap<>();

    @Override
    public Map<String, Object> asMap() {
      return attributes;
    }

    @Override
    public Model addAttribute(String attributeName, Object attributeValue) {
      attributes.put(attributeName, attributeValue);
      return this;
    }

    @Override
    public Model addAttribute(Object attributeValue) {
      return null;
    }

    @Override
    public Model addAllAttributes(Collection<?> attributeValues) {
      return null;
    }

    @Override
    public Model addAllAttributes(Map<String, ?> attributes) {
      return null;
    }

    @Override
    public Model mergeAttributes(Map<String, ?> attributes) {
      return null;
    }

    @Override
    public boolean containsAttribute(String attributeName) {
      return false;
    }

    @Override
    public Object getAttribute(String attributeName) {
      return null;
    }
  };

}
