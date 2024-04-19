package de.hhu.propra.roommate.application.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final Set<String> admins = new HashSet<>();

  public UserService() {
    String admins = System.getenv("ADMINS");
    if (admins != null) {
      Collections.addAll(this.admins, admins.split(","));
    }
  }

  private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = defaultService.loadUser(userRequest);
    Set<GrantedAuthority> authorities = new HashSet<>(oAuth2User.getAuthorities());

    String login = oAuth2User.getAttribute("login");
      if (admins.contains(login)) {
          authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      }

    return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "id");
  }

}
