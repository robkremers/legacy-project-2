# Beschrijving Implementatie Forgerock OpenAM LDAP

[[_TOC_]]

## Introductie

Momenteel wordt t.b.v. de gebruikersadministratie in Suwinet Inkijk Classic opendj-6.5.2 van Forgerock toegepast 
als access management implementatie.  
Deze functionaliteit baseert op een LDAP (Lightweight Directory Access Protocol) database met een predefined REST API.
LDAP wordt met name gebruikt om gebruikersinformatie op te slaan: gebruikers, attributen, groups-privileges, etc.

## Referenties en documenten

Algemeen:
- https://www.forgerock.com/
- https://backstage.forgerock.com/docs/ds/6.5/install-guide/
- https://backstage.forgerock.com/docs/am/6.5
  - Extensive documentation
- https://backstage.forgerock.com/docs/am/6.5/quick-start-guide/
- [AM-6.5-Dev-Guide.pdf](./documents/AM-6.5-Dev-Guide.pdf)
  - Guide to developing REST clients and scripts.
- [openam-6.5 download](https://backstage.forgerock.com/downloads/get/familyId:am/productId:am/minorVersion:6.5/version:6.5.2.3/releaseType:security-patch/patchVersion:202101/distribution:zip)
  - Registreren voordat download mogelijk is.
- https://backstage.forgerock.com/docs/am/6.5/reference/

BKWI specifieke informatie:
- [Overview of REST for AccessManagement 6.5.2.2](https://office.bkwi.nl/sas/sas-documentation/useradmin/accessmanagement-65-rest-calls.html)
  - Gedetailleerd overzicht over access tot en handling van Forgerock OpenAM LDAP via REST API.
  - Handling voorbeelden in de vorm van curl commands.
- $ws/sas/shared/commons/src/main/resources/config/ENV-a-DEVELOPER-common.properties
  - Hier is o.a. gedefinieerd: 
    - **OPENAM_HOST=localhost**
- /$ws/sas/sas/openam-login/README-am-installation.txt

## Huidige versie

ForgeRock OpenAm:
- Lokaal, KIT, productie:
  - 6.5.2
- BTO:
  - 6.5.3.

## Huidige classes in sas, die LDAP functionaliteit aanroepen

Rapportage:
- $ws/sas/services/useradmin/src/main/java/nl/bkwi/services/useradmin/mandateduser/MandatedUserService.java

Algemeen:
- $ws/sas/inkijk-login/webapp/src/main/java/nl/bkwi/inkijk/login/LdapLoginService.java
  - Service to get account information from a ldap server. 
  - The server url is either configured using the system properties ldap.protocol, ldap.host and ldap.port or 
    using the default connection to localhost. The credentials are hardcoded to minimize dependencies and maximize simplicity.
    - Hierin zijn lokaal username, password, portnummer gedeclareerd. 
- Classes:
- $ws/sas/services/useradmin/src/main/java/nl/bkwi/services/useradmin/security/
  - auth/openam/AuthenticatorImpl.java
- $ws/sas/services/useradmin/src/main/java/nl/bkwi/services/useradmin/security/
  - policy/openam/
    - PolicyEnforcerImpl.java
    - PolicyAdapterOpenAmImpl.java

## Lokaal starten van ForgeRock OpenAm LDAP

### SI Classic:

```shell
# Start the LDAP service
d -l
```

De content is dan +/- gelijk aan (Mandated Users is geval apart):
- $ws/sas/shared/utils/vagrant/suwibroker/import_users.ldif

Deze content wordt, net als de functierollen voor SI Upgrade, ingeladen in LDAP door een proces in de bouwstraat.  
Pas nadat deze uitgevoerd is kan de nieuwe LDAP content gedownload worden voor SI Upgrade.  
De ontwerpers wilden deze procedure niet veranderen.

### SI Upgrade:

Dit wordt via docker-compose.yml opgestart. De volgende container is dan aanwezig: **inkijk_ldap_1**.  
Deze is als volgt te benaderen:

#### Definitie in docker-compose.yml:
```yaml
services:
  ldap:
    image: "${REGISTRY}/database_opendj:${TAG}"
    expose:
      - "4444"
      - "50389"
      - "50636"
    healthcheck:
      test: ["CMD", "/opt/opendj/bin/ldapsearch", "--port", "50389", "--bindDN", "cn=Directory Manager", "--bindPassword", "vagrant", "--baseDN", "ou=bkwi,o=suwi,c=nl", "--searchScope", "one", "(objectClass=organizationalunit)", "dn" ]
      interval: 30s
      timeout: 5s
      retries: 1
  proxy:
    image: "${REGISTRY}/proxy_apache:${TAG}"
    ports:
      - "${APPLICATION_PORT}:80"
      - "${SECURE_PORT}:443"
    volumes:
      - ${ws}/sas/sas/pom/target/gwt:/usr/local/apache2/htdocs
```

Note dat de container in de definitie geen dependency heeft en dus via het image separaat op te starten is.  
Hopelijk.

#### Inloggen in de docker container.
```shell
$ docker container exec -it inkijk_ldap_1 /bin/sh
```

## Vragen

- Vraag: aan welke voorwaarden moeten gebruikersnamen voldoen? Qua lengte, qua uniciteit, etc.
  - Dit moet in de docu en code nagekeken worden.
  - In het algemeen kan gezegd worden:
    - In LDAP wordt de informatie als json opgeslagen. Daarom zal b.v. lengte in het algemeen geen probleem zijn.
    - Lengte, uniciteit zal afhankelijk zijn van de applicatie, i.e. de java functionaliteit:
      - Hoe wordt momenteel de content geselecteerd.
      - Wat is het type van de variabelen, die de content ontvangen.
  - Zal overeenkomstig de huidige functionaliteit zijn. Zie de lijst van relevante classes hierboven.

[back to main](../README.md) |
[previous](./3_Beschrijving_huidige_situatie_SI_Classic.md) |
[next](./5_DeveloperInfo.md)