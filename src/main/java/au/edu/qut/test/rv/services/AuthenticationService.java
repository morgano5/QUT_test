package au.edu.qut.test.rv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService implements AuthenticationProvider {

  private final String user;
  private final String password;

  @Autowired
  public AuthenticationService(
      @Value("${au.edu.qut.user}") String user, @Value("${au.edu.qut.password}") String password) {
    Objects.requireNonNull(user);
    Objects.requireNonNull(password);
    this.user = user;
    this.password = password;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    return user.equals(authentication.getName()) && password.equals(authentication.getCredentials())
        ? new UsernamePasswordAuthenticationToken(
            authentication.getPrincipal(), authentication.getCredentials(), null)
        : null;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return aClass == UsernamePasswordAuthenticationToken.class;
  }
}
