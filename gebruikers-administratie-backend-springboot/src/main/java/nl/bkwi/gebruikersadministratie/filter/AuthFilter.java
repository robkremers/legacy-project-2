package nl.bkwi.gebruikersadministratie.filter;

import nl.bkwi.gebruikersadministratie.systemuser.SystemUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer.Element;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
public class AuthFilter implements WebFilter {


  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    // LET OP!!! we do not filter OPTIONS requests, to make sure preflight CORS requests go through. This requires review in security context.
    if (HttpMethod.OPTIONS.equals(request.getMethod())) {
      return chain.filter(exchange);
    }

    Optional<String> authenticated = request.getPath()
      .elements().stream()
      .map(Element::value)
      .filter("backend"::equals).findAny();
    if (!authenticated.isPresent()) {
      return chain.filter(exchange);
    }
    return handleAuthentication(exchange, chain, request);
  }

  private Mono<Void> handleAuthentication(ServerWebExchange exchange, WebFilterChain chain, ServerHttpRequest request) {
    HttpHeaders headers = request.getHeaders();
    try {
      String dn = extractDNHeader(headers, "am_dn");
      LdapName distinguishedName = new LdapName(dn);
      String userName = getCommonName(distinguishedName);
      String userId = extractUIDHeader(headers, "am_uid");
      SystemUser systemUser = new SystemUser(userId, userName, dn);
      exchange.getAttributes().put("principal", systemUser);
      return chain.filter(exchange);
    } catch (InvalidNameException | IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  private String extractDNHeader(HttpHeaders headers, String key) {
    List<String> headerList = headers.get(key);
    if (headerList != null && !headerList.isEmpty()) {
      return getDistinguishedName(headerList.get(0));
    } else {
      throw new IllegalArgumentException("Not found in header: " + key);
    }
  }

  private String extractUIDHeader(HttpHeaders headers, String key) {
    List<String> headerList = headers.get(key);
    if (headerList != null && !headerList.isEmpty()) {
      return headerList.get(0);
    } else {
      throw new IllegalArgumentException("Not found in header: " + key);
    }
  }

  private String getDistinguishedName(String dnName) {
    // Hier nog heel goed naar kijken. Als OpenAM base64 doorgeeft dan eigenlijk alleen base64 toestaan
    // Check if we have a base64 encoded value or not (comma isn't allowed in base64)
    if (dnName.contains(",")) {
      return new String(dnName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).replace("&#38;", "&");
    } else {
      return new String(Base64.getDecoder().decode(dnName), StandardCharsets.UTF_8).replace("&#38;", "&");
    }
  }

  private String getCommonName(LdapName dn) {
    for (Rdn part : dn.getRdns()) {
      if (part.getType().equals("cn")) {
        return part.getValue().toString();
      }
    }
    throw new IllegalArgumentException("Could not find name in dn " + dn);
  }

}
