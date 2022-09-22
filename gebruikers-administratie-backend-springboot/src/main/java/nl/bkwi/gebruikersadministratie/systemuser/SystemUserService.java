package nl.bkwi.gebruikersadministratie.systemuser;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class SystemUserService {

  public Mono<SystemUserDTO> getSystemUser(Optional<SystemUser> systemUser) {
    SystemUserDTO systemUserDTO = new SystemUserDTO();
    if (systemUser.isPresent()) {
      systemUserDTO.setNaam(systemUser.get().getName());
      return Mono.just(systemUserDTO);
    }
    return Mono.empty();
  }
}
