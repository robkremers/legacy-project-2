package nl.bkwi.gebruikers.administratie.api.gebruiker;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import lombok.extern.slf4j.Slf4j;
import nl.bkwi.gebruikers.administratie.api.gebruiker.exceptions.GebruikerNietGevondenException;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.services.useradmin.dao.OpenDJUserAdminDao;
import nl.bkwi.services.useradmin.domain.LdapUser;
import nl.bkwi.services.useradmin.ldap.LDAPException;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GebruikerService {

    private String baseValue;

    private OpenDJUserAdminDao userAdminDao;

    private int passwordLength;

    public GebruikerService(
            OpenDJUserAdminDao userAdminDao,
            @Value("${spring.ldap.base}") String baseValue,
            @Value("${password.length}") int passwordLength
    ) {
        this.userAdminDao = userAdminDao;
        this.baseValue = baseValue;
        this.passwordLength = passwordLength;
    }

    public Mono<GebruikerDTO> getGebruiker(String dn, String uid)
            throws LDAPException, InvalidNameException {
        List<GebruikerDTO> gebruikers = retrieveSelectedGebruikers(dn, Set.of(uid));
        return Mono.justOrEmpty(gebruikers.stream().findAny());
    }

    private List<GebruikerDTO> retrieveSelectedGebruikers(String dn, Set<String> uids)
            throws LDAPException, InvalidNameException {
        return getLdapUsers(dn, uids).stream()
                .map(LdapUserToGebruikerDTOMapper::mapLdapUser2GebruikerDTO)
                .toList();
    }

    private List<LdapUser> getLdapUsers(String dn, Set<String> uids) throws LDAPException, InvalidNameException {
        return userAdminDao.searchUsers(new LdapName(dn), composeBaseDN(dn), uids);
    }

    private LdapUser getLdapUserByUid(String dn, String uid) throws LDAPException, InvalidNameException {
        List<LdapUser> ldapUsers = getLdapUsers(dn, Set.of(uid));
        Iterator<LdapUser> iterator = ldapUsers.iterator();
        if (iterator.hasNext()) {
            LdapUser user = iterator.next();

            if (iterator.hasNext()) {
                throw new LDAPException("Collection contains more than one user");
            }

            return user;
        }
        return null;

    }

    private LdapName composeBaseDN(String dn) throws InvalidNameException {
        String ous = Arrays.stream(dn.split(",")).filter(x -> x.startsWith("ou="))
                .collect(Collectors.joining(","));
        String base = ous + "," + baseValue;
        return new LdapName(base);
    }

    public Flux<GebruikerDTO> getGebruikers(String dn) throws LDAPException, InvalidNameException {
        return Flux.fromIterable(retrieveGebruikers(dn));
    }

    public void manualUpdateUserPassword(String dn, String uid, String newPassword)
            throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
        LdapName admin = new LdapName(dn);
        LdapUser user = getLdapUserByUid(dn, uid);

        if ("".equals(newPassword.trim())) {
            throw new IllegalArgumentException(
                    "Nieuw wachtwoord mag niet alleen uit spaties bestaan of leeg zijn.");
        }

        if (user == null) {
            throw new GebruikerNietGevondenException(
                    String.format("LdapUser with userId %s not found", uid));
        }

        userAdminDao.updateUserPassword(admin, user, newPassword);

    }

    public Mono<PasswordDTO> resetUserPassword(String dn, String uid)
            throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
        LdapName admin = new LdapName(dn);
        LdapUser user = getLdapUserByUid(dn, uid);

        if (user == null) {
            throw new GebruikerNietGevondenException(
                    String.format("LdapUser with userId %s not found", uid));
        }

        PasswordDTO passwordDTO = new PasswordDTO(generatePassword());
        userAdminDao.updateUserPassword(admin, user, passwordDTO.getPassword());

        return Mono.just(passwordDTO);
    }

    private List<GebruikerDTO> retrieveGebruikers(String dn)
            throws LDAPException, InvalidNameException {
        return userAdminDao.listUsers(new LdapName(dn), composeBaseDN(dn)).stream()
                .map(LdapUserToGebruikerDTOMapper::mapLdapUser2GebruikerDTO)
                .toList();
    }

  private String generatePassword() {
    char[] characterRange = IntStream.rangeClosed('A', 'z')
            .mapToObj( i -> "" + (char) i)
            .filter( string -> string.charAt(0) <= 'Z' || string.charAt(0) >= 'a' )
            .collect(Collectors.joining())
            .toCharArray();
    RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().selectFrom(characterRange).build();
    return pwdGenerator.generate(passwordLength);
  }
}
