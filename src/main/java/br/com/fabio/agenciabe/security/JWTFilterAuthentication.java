package br.com.fabio.agenciabe.security;

import br.com.fabio.agenciabe.data.UserDetailsData;
import br.com.fabio.agenciabe.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTFilterAuthentication extends UsernamePasswordAuthenticationFilter {

  public static final int TOKEN_EXPIRES = 600_000;
  public static final String TOKEN_PASSWORD = "0776d591-6710-46ee-a5ba-8efdab236d9e";

  private final AuthenticationManager authenticationManager;


  public JWTFilterAuthentication(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
    try{
      User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
      return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword(), new ArrayList<>()));
    }catch (IOException e){
      throw new RuntimeException("Falha ao autenticar o usuário", e);
    }

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    UserDetailsData userDetailsData = (UserDetailsData) authResult.getPrincipal();

    String token = JWT.create()
        .withSubject(userDetailsData.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRES))
        .sign(Algorithm.HMAC512(TOKEN_PASSWORD));

    response.getWriter().write(token);
    response.getWriter().flush();
  }
}
