package nl.bkwi.gebruikersadministratie.systemuser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SystemUserDTOTest {

  @Test
  void testEqualsAndHashcodeEqual() {
    SystemUserDTO systemUserDTO1 = new SystemUserDTO();
    systemUserDTO1.setNaam("naam");
    SystemUserDTO systemUserDTO2 = new SystemUserDTO();
    systemUserDTO2.setNaam("naam");

    assertEquals(systemUserDTO1.hashCode(), systemUserDTO2.hashCode());
    assertEquals(systemUserDTO1, systemUserDTO2);
  }

  @Test
  void testEqualsAndHashcodeNotEqual() {
    SystemUserDTO systemUserDTO1 = new SystemUserDTO();
    systemUserDTO1.setNaam("naam1");
    SystemUserDTO systemUserDTO2 = new SystemUserDTO();
    systemUserDTO2.setNaam("naam2");

    assertNotEquals(systemUserDTO1.hashCode(), systemUserDTO2.hashCode());
    assertNotEquals(systemUserDTO1, systemUserDTO2);
  }

  @Test
  void testToString() {
    SystemUserDTO systemUserDTO = new SystemUserDTO();
    systemUserDTO.setNaam("naam");

    assertEquals("naam", systemUserDTO.toString());
  }
}