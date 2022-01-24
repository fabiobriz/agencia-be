package br.com.fabio.agenciabe.service;

import br.com.fabio.agenciabe.data.UserDetailsData;
import br.com.fabio.agenciabe.model.User;
import br.com.fabio.agenciabe.model.UserRepository;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService{

  private final UserRepository repository;

  public UserDetailsServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = repository.findByLogin(username);
    if(user.isEmpty()){
      throw new UsernameNotFoundException("Usuário ["+ username +"]não encontrado.");
    }
    return new UserDetailsData(user);
  }
}
