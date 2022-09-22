package nl.bkwi.gebruikersadministratie.systemuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SystemUserController.class)
@Import(SystemUserService.class)
class SystemUserControllerTest {
    public static final String BKWI_BEHEERDER_DN = "cn=BKWI Beheerder,ou=BKWI,o=suwi,c=nl";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private SystemUserService systemUserService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void currentUser() {
        String beheerder = BKWI_BEHEERDER_DN;

        webTestClient.get()
                .uri("/backend/system-user")
                .accept(MediaType.APPLICATION_JSON)
                .header("am_uid", "uid")
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SystemUserDTO.class)
                .value(x -> x.getNaam(), equalTo("BKWI Beheerder"));

    }
}