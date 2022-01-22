package br.com.fabio.agenciabe.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  public Optional<User> findByLogin(String string);

}
