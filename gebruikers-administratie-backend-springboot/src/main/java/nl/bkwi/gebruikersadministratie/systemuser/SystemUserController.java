package nl.bkwi.gebruikersadministratie.systemuser;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/backend/system-user")
public class SystemUserController {

  private final SystemUserService systemUserService;

  public SystemUserController(SystemUserService systemUserService) {
    this.systemUserService = systemUserService;
  }


  @GetMapping()
  public Mono<SystemUserDTO> currentUser(@RequestAttribute("principal") Optional<SystemUser> systemUser) {
    return systemUserService.getSystemUser(systemUser);
  }

}
