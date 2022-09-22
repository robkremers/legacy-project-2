package nl.bkwi.gebruikersadministratie.gebruiker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class UiGebruikerDTOTest {

    @Test
    void testGettersForCoverage() {
        UiGebruikerDTO uiGebruikerDTO = testUiGebruikerBuilder().build();

        assertEquals("voornaam", uiGebruikerDTO.getVoornaam());
        assertEquals("achternaam", uiGebruikerDTO.getAchternaam());
        assertEquals("userId", uiGebruikerDTO.getUserId());
        assertEquals("initialen", uiGebruikerDTO.getInitialen());
        assertEquals("email", uiGebruikerDTO.getEmail());
        assertEquals("telefoonnummer", uiGebruikerDTO.getTelefoonnummer());
        assertEquals("afdeling", uiGebruikerDTO.getAfdeling());
        assertEquals("employeeNr", uiGebruikerDTO.getEmployeeNr());
    }

    @Test
    void testEqualsAndHashcode() {
        UiGebruikerDTO uiGebruikerDTO1 = testUiGebruikerBuilder().build();
        UiGebruikerDTO uiGebruikerDTO2 = testUiGebruikerBuilder().build();

        assertEquals(uiGebruikerDTO1.hashCode(), uiGebruikerDTO2.hashCode());
        assertEquals(uiGebruikerDTO1, uiGebruikerDTO2);
    }

    @Test
    void testEqualsAndHashcodeNotEqualUserIdIsNotEqual() {
        UiGebruikerDTO uiGebruikerDTO1 = testUiGebruikerBuilder().build();
        UiGebruikerDTO uiGebruikerDTO2 = testUiGebruikerBuilder().userId("anders").build();

        assertNotEquals(uiGebruikerDTO1.hashCode(), uiGebruikerDTO2.hashCode());
        assertNotEquals(uiGebruikerDTO1, uiGebruikerDTO2);
    }

    @Test
    void testEqualsAndHashcodeEqualUserIdAndereVoornaamIsEqual() {
        UiGebruikerDTO uiGebruikerDTO1 = testUiGebruikerBuilder().build();
        UiGebruikerDTO uiGebruikerDTO2 = testUiGebruikerBuilder().voornaam("anders").build();

        assertEquals(uiGebruikerDTO1.hashCode(), uiGebruikerDTO2.hashCode());
        assertEquals(uiGebruikerDTO1, uiGebruikerDTO2);
    }

    @Test
    void testEqualsAndHashcodeEqualUserIdAndereAchternaamIsEqual() {
        UiGebruikerDTO uiGebruikerDTO1 = testUiGebruikerBuilder().build();
        UiGebruikerDTO uiGebruikerDTO2 = testUiGebruikerBuilder().achternaam("anders").build();

        assertEquals(uiGebruikerDTO1.hashCode(), uiGebruikerDTO2.hashCode());
        assertEquals(uiGebruikerDTO1, uiGebruikerDTO2);
    }

    private UiGebruikerDTO.UiGebruikerDTOBuilder testUiGebruikerBuilder() {
        return UiGebruikerDTO.builder()
            .achternaam("achternaam")
            .afdeling("afdeling")
            .email("email")
            .initialen("initialen")
            .telefoonnummer("telefoonnummer")
            .userId("userId")
            .voornaam("voornaam")
            .employeeNr("employeeNr");
    }
}