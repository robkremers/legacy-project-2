package nl.bkwi.gebruikersadministratie.systemuser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class SystemUserServiceTest {

  @InjectMocks
  private SystemUserService systemUserService;

  @Test
  void getSystemUserMetJuisteNaamTest() {
    SystemUser systemUser = new SystemUser("userid", "naam", "dn");

    Mono<SystemUserDTO> systemUserDTOMono = systemUserService.getSystemUser(
        Optional.of(systemUser));

    SystemUserDTO systemUserDTO = new SystemUserDTO();
    systemUserDTO.setNaam("naam");
    StepVerifier.create(systemUserDTOMono)
        .expectNext(systemUserDTO)
        .verifyComplete();
  }

  @Test
  void getSystemUserEmptyTest() {
    Mono<SystemUserDTO> systemUserDTOMono = systemUserService.getSystemUser(Optional.empty());
    assertEquals(Mono.empty(), systemUserDTOMono);
  }
}