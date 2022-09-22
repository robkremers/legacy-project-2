package nl.bkwi.gebruikersadministratie;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GebruikersAdministratieApplicationTests {
	public static final String BKWI_BEHEERDER_DN = "cn=BKWI Beheerder,ou=BKWI,o=suwi,c=nl";

	@Test
	void contextLoads() {
    assertNotNull(this);
	}

}
