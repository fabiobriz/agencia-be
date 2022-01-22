package br.com.fabio.agenciabe.controller;

import br.com.fabio.agenciabe.model.User;
import br.com.fabio.agenciabe.model.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController{

  private final UserRepository repository;
  private final PasswordEncoder encoder;

  public UserController(UserRepository repository, PasswordEncoder encoder) {
    this.repository = repository;
    this.encoder = encoder;
  }

  @GetMapping
  public ResponseEntity<List<User>> getAll(){
    return ResponseEntity.ok(repository.findAll());
  }

  @PostMapping("/create")
  public ResponseEntity<User> create(@RequestBody User user){
    user.setPassword(encoder.encode(user.getPassword()));
    return ResponseEntity.ok(repository.save(user));
  }
  @GetMapping("validate-password")
  public ResponseEntity<Boolean> validatePassword(@RequestParam String login, @RequestParam String password){

    Optional<User> optUser = repository.findByLogin(login);
    if(optUser.isEmpty()){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    boolean valid = encoder.matches(password, optUser.get().getPassword());

    HttpStatus status = (valid) ? HttpStatus.OK: HttpStatus.UNAUTHORIZED;
    return ResponseEntity.status(status).body(valid);

  }




}
