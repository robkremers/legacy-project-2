package nl.bkwi.gebruikers.administratie.api.gebruiker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import lombok.extern.slf4j.Slf4j;
import nl.bkwi.BeheerderConstants;
import nl.bkwi.gebruikers.administratie.api.gebruiker.exceptions.GebruikerNietGevondenException;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.services.useradmin.dao.OpenDJUserAdminDao;
import nl.bkwi.services.useradmin.domain.LdapUser;
import nl.bkwi.services.useradmin.ldap.LDAPException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GebruikerServiceTest {

    @Autowired
    private GebruikerService gebruikerService;

    @Mock
    private OpenDJUserAdminDao userAdminDao;

    private static final String username = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    private static final String password = "{SSHA512}INVALID_3cQN7trzRh0pnoHfSH7eGh5DHnXHuQOCIhRDH/uMKxL/ZLiEzJWTWyFiy3lbvIP1aEoPxgAH2CCGv2xy6ZN8HFBG";

  @Test
  void getGebruikersTest() throws InvalidNameException, nl.bkwi.services.useradmin.ldap.LDAPException {

    // Given
    String beheerder_dn = BeheerderConstants.BKWI_BEHEERDER_DN;
    List<LdapUser> mockGebruikers = getMockGebruikers();
    GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);

    Mockito.when(userAdminDao.listUsers(any(), any())).thenReturn(mockGebruikers);

    Flux<GebruikerDTO> gebruikerFlux = gebruikerService1.getGebruikers(beheerder_dn);

        assertNotNull(gebruikerFlux);
        StepVerifier.create(gebruikerFlux)
                .expectNext(LdapUserToGebruikerDTOMapper.mapLdapUser2GebruikerDTO(mockGebruikers.get(0)))
                .expectNext(LdapUserToGebruikerDTOMapper.mapLdapUser2GebruikerDTO(mockGebruikers.get(1)))
                .expectComplete().verify();
    }

    private List<LdapUser> getMockGebruikers() throws InvalidNameException {
        List<LdapUser> result = new ArrayList<>();

        LdapName ldapName1 = new LdapName("cn=user1,ou=BKWI,o=suwi,c=nl");
        LdapUser ldapUser1 = new LdapUser(ldapName1);
        ldapUser1.setUserId("u1");
        ldapUser1.setFirstName("BKWI");
        ldapUser1.setLastName("gebruiker1");

        LdapName ldapName2 = new LdapName("cn=user2,ou=BKWI,o=suwi,c=nl");
        LdapUser ldapUser2 = new LdapUser(ldapName2);
        ldapUser2.setUserId("u2");
        ldapUser2.setFirstName("BKWI");
        ldapUser2.setLastName("gebruiker2");

        result.add(ldapUser1);
        result.add(ldapUser2);

        return result;
    }

    @Test
    void getGebruikerTest() throws InvalidNameException, LDAPException {
        // Given
        String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
        String uid = "uid";
        List<LdapUser> mockGebruikers = getMockGebruikers();
        GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);

        // When
        Mockito.when(userAdminDao.searchUsers(any(), any(), any())).thenReturn(mockGebruikers);

        // Then
        Mono<GebruikerDTO> gebruiker = gebruikerService1.getGebruiker(dn, uid);

        assertNotNull(gebruiker);
        GebruikerDTO gebruikerDTO = gebruiker.block();
        assertNotNull(gebruikerDTO);
        assertEquals("gebruiker1", gebruikerDTO.getAchternaam());
    }

  @Test
  void updateUserPasswordTest()
    throws InvalidNameException, GebruikerNietGevondenException, LDAPException {

    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "u1";
    String newPassword = "eennieuwwachtwoord";
    List<LdapUser> mockGebruikers = getMockGebruikers();
    GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);

    // When
    Mockito.when(userAdminDao.searchUsers(any(), any(), any())).thenReturn(mockGebruikers.subList(0, 1));

        // Then
        gebruikerService1.manualUpdateUserPassword(dn, uid, newPassword);

        LdapName admin = new LdapName(dn);
        LdapUser user = mockGebruikers.get(0);
        verify(userAdminDao, times(1)).updateUserPassword(admin, user, newPassword);
    }

  @Test
  void testResetUserPassword()
    throws InvalidNameException, GebruikerNietGevondenException, LDAPException {
    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "uid";
    List<LdapUser> mockGebruikers = getMockGebruikers();
    String newPassword = "newPassword";

    GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);

    // When
    // public Mono<PasswordDTO> resetUserPassword --> private LdapUser getLdapUserByUid --> getLdapUsers --> userAdminDao.searchUsers
        Mockito.when(userAdminDao.searchUsers(any(), any(), any())).thenReturn(mockGebruikers.subList(0, 1));

        // Then
        // To be tested is: a) a password is returned, b) the password is saved in ldap.
        Mono<PasswordDTO> passwordDTOMono = gebruikerService1.resetUserPassword(dn, uid);

        PasswordDTO actualPasswordDTO = passwordDTOMono.block();

        assertNotNull(actualPasswordDTO.getPassword());
        assertEquals(10, actualPasswordDTO.getPassword().length());

        LdapName admin = new LdapName(dn);
        LdapUser user = mockGebruikers.get(0);
        verify(userAdminDao, times(1)).updateUserPassword(admin, user, actualPasswordDTO.getPassword());
    }

    @Test
    void updateUserPasswordTestGebruikerMetUidNietGevondenThrowsException() throws LDAPException {
        // Given
        String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
        String uid = "onbekendegebruiker";
        String newPassword = "eennieuwwachtwoord";
        GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);

        // When
        // mock het retourneren van een lege lijst van gebruikers
        Mockito.when(userAdminDao.searchUsers(any(), any(), any())).thenReturn(Collections.emptyList());

        // Then
        GebruikerNietGevondenException thrown = Assertions.assertThrows(
                GebruikerNietGevondenException.class, () -> {
                    // When
                    gebruikerService1.manualUpdateUserPassword(dn, uid, newPassword);
                });

        Assertions.assertEquals("LdapUser with userId onbekendegebruiker not found",
                thrown.getMessage());
    }

  @Test
  void testResetUserPasswordGebruikerMetUidNietGevondenThrowsException() throws InvalidNameException, LDAPException {
    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "onbekendegebruiker";
    List<LdapUser> mockGebruikers = getMockGebruikers();

    GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);
    // When
    Mockito.when(userAdminDao.searchUsers(any(), any(), any())).thenReturn(Collections.emptyList());

    // Then
        GebruikerNietGevondenException thrown = Assertions.assertThrows(
                GebruikerNietGevondenException.class, () -> {
                    // When
                    gebruikerService1.resetUserPassword(dn, uid);
                });

        Assertions.assertEquals("LdapUser with userId onbekendegebruiker not found",
                thrown.getMessage());
    }

    @Test
    void updateUserPasswordTestMetLeegWachtwoordThrowsException()
            throws LDAPException, InvalidNameException {
        // Given
        String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
        String uid = "u1";
        String newPassword = "";
        List<LdapUser> mockGebruikers = getMockGebruikers();
        GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);

        // When
        Mockito.when(userAdminDao.searchUsers(any(), any(), any()))
                .thenReturn(mockGebruikers.subList(0, 1));

        // Then
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    // When
                    gebruikerService1.manualUpdateUserPassword(dn, uid, newPassword);
                });

        Assertions.assertEquals("Nieuw wachtwoord mag niet alleen uit spaties bestaan of leeg zijn.",
                thrown.getMessage());
    }

    @Test
    void updateUserPasswordTestMetWachtwoordMetAlleenSpatiesThrowsException()
            throws LDAPException, InvalidNameException {
        // Given
        String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
        String uid = "u1";
        String newPassword = "    ";
        List<LdapUser> mockGebruikers = getMockGebruikers();
        GebruikerService gebruikerService1 = new GebruikerService(userAdminDao, "", 10);

        // When
        Mockito.when(userAdminDao.searchUsers(any(), any(), any()))
                .thenReturn(mockGebruikers.subList(0, 1));

        // Then
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    // When
                    gebruikerService1.manualUpdateUserPassword(dn, uid, newPassword);
                });

        Assertions.assertEquals("Nieuw wachtwoord mag niet alleen uit spaties bestaan of leeg zijn.",
                thrown.getMessage());
    }
}
