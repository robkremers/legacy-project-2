package nl.bkwi.gebruikers.administratie.api.gebruiker.ldap;

import static org.forgerock.opendj.ldap.Connections.newInternalConnectionFactory;

import io.reactivex.Flowable;
import nl.bkwi.services.useradmin.ldap.LdapConnectionPool;
import org.forgerock.opendj.ldap.Connection;
import org.forgerock.opendj.ldap.ConnectionFactory;
import org.forgerock.opendj.ldap.LdapException;
import org.forgerock.opendj.ldap.MemoryBackend;
import org.forgerock.opendj.ldap.ResultCode;
import org.forgerock.opendj.ldap.messages.AbstractRequestVisitor;
import org.forgerock.opendj.ldap.messages.ExtendedRequest;
import org.forgerock.opendj.ldap.messages.PasswordModifyExtendedRequest;
import org.forgerock.opendj.ldap.messages.PasswordModifyExtendedResult;
import org.forgerock.opendj.ldap.messages.Request;
import org.forgerock.opendj.ldap.messages.RequestVisitor;
import org.forgerock.opendj.ldap.messages.Response;
import org.forgerock.opendj.ldap.messages.Responses;
import org.forgerock.opendj.ldif.EntryReader;
import org.forgerock.opendj.ldif.LdifEntryReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdapConnectionFactoryStub {
  private static final Logger LOG = LoggerFactory.getLogger(LdapConnectionFactoryStub.class);
  /**
   * This class is used to get a connectionFactory that reads its data from a fixed data set.
   *
   * @return a connectionFactory with connections to get fixed test data
   */
  public static LdapConnectionPool getConnectionFactory() {
    try {
      EntryReader reader = new LdifEntryReader(
          LdapConnectionFactoryStub.class.getResourceAsStream("/data/ldap-testdata.ldif"));
      MemoryBackend backend = new MemoryBackend(reader);

      // Zie commentaar in de methode createVisitor() om onderstaande regel te gebruiken ipv bovenstaande.
       ConnectionFactory connectionFactory = newInternalConnectionFactory((msgid, request) -> request.accept(createVisitor(backend), null));

      return new LdapConnectionPool() {
        @Override
        public Connection getConnection() throws LdapException {
          return connectionFactory.getConnection();
        }

        @Override
        public void close() {
          connectionFactory.close();
        }
      };
    } catch (Exception e) {
      LOG.error("Failed to create LdapConnectionPool", e);
      return null;
    }
  }

  /*
   * Deze oplossing hebben we van Auke gekegen en is een poging om deze foutmelding in unittesten te krijgen:
   *
   * nl.bkwi.gebruikers.administratie.api.gebruiker.ldap.LDAPException: Protocol Error: Unsupported request type: EXTENDED
   *
   * Deze oplossing heb ik helaas nog niet aan de praat gekregen. Je zou op de plek waar je nu de ConnectionFactory maakt
   * het volgende moeten zetten:
   *
   * ConnectionFactory connectionFactory = newInternalConnectionFactory((msgid, request) -> request.accept(createVisitor(backend), null));
   *
   * @param backend
   * @return
   */
  private static RequestVisitor<Flowable<Response>, Void, LdapException> createVisitor(MemoryBackend backend) {
    RequestVisitor<Flowable<Response>, Void, LdapException> visitor = new AbstractRequestVisitor<>() {
      @Override
      public Flowable<Response> visitRequest(Void context, ExtendedRequest<?> request) throws LdapException {
        if (request instanceof PasswordModifyExtendedRequest) {
          PasswordModifyExtendedRequest passwordModifyExtendedRequest = (PasswordModifyExtendedRequest) request;
          byte[] newPassword = passwordModifyExtendedRequest.getNewPassword();

          PasswordModifyExtendedResult response = Responses.newPasswordModifyExtendedResult(ResultCode.SUCCESS);
          response.setGeneratedPassword(newPassword);
          // TODO: actually store the password in the ldap backend ...
          return Flowable.just(response);
        }
        return super.visitRequest(context, request);
      }

      @Override
      protected Flowable<Response> visitAnyRequest(Void context, Request request) throws LdapException {
        return backend.handleRequest(request);
      }
    };
    return visitor;
  }

}
