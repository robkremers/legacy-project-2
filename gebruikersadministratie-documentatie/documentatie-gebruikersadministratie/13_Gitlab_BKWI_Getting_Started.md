# Gitlab bij BKWI
In deze (onvolledige) pagina wil ik de aantekeningen die ik gemaakt heb bij het van start gaan met Gitlab bij BKWI
even delen met de rest. Wellicht zou dit de basis kunnen zijn voor een echte "Getting Started" voor Gitlab. 
Met Gitlab wordt natuurlijk de code beheerd via git, maar gitlab doet veel meer waaronder een Continuous Integration 
tool en repository manager. Bij BKWI kan je een nieuw gitlab project maken op basis van verschillende templates,
waardoor een aantal dingen direct ingeregeld zijn. De kennis die ik daarover bij elkaar gesprokkeld heb staat hier
beschreven.

# Pipeline (Continuous Integration / CI)
Om een gitlab repository in de gitlab pipeline te draaien moet de pipeline geconfigureerd worden. De configuratie van 
de gitlab pipeline staat in dit bestand


.gitlab-ci.yml

in de root folder van het repository


InfraOPS streeft er naar om zoveel mogelijk configuratie details via template projecten in te stellen. Dit heeft als 
grote voordeel dat niet elke ontwikkelaar deze details hoeft te kennen, maar ook dat deze configuratiedetails centraal 
beheerd kunnen worden, zodat wijzigingen automatisch worden toegepast door de projecten die er afhankelijk van zit. Om 
gebruik te kunnen maken van een template project moeten de eerste 3 regels van de yaml file als volgt zijn:

include:
- project: "bkwi/beheer/infraops/ontwikkeltooling/gitlab-ci"
  file: "gitlab-ci-yml/<some bkwi template.yml>”

## Gitlab Templates
Op dit moment zijn de volgende templates gemaakt door infraops

1. docker-deploy.yml
2. docker-image.yml
3. docker-maven-deploy.yml
4. docker-npm-deploy.yml
5. docker-stub-deploy.yml
6. docker-stub-maven-deploy.yml
7. image-deploy.yml
8. image-stub-deploy.yml
9. maven-deploy.yml
10. maven-image-deploy.yml
11. microservice-deploy.yml

Welke template je nodig hebt hangt af van je project. Voor de volgende typen projecten kan je dit aanhouden:

- java backend project om iets te ontsluiten => Pak template 11 om een micro service te deployen. Hiermee kan je bijvoorbeeld een spring boot backend die een endpoint ontsluit hosten
- frontend project gebaseerd op NPM => Pak template 4. Hiermee kan je bijvoorbeeld een angular frontend hosten
- database of ldap server waar je een docker-compose file van hebt => Pak template 8. Hiermee kan je bijvoorbeeld een mysql database of ldap server hosten

De templates staan gedefinieerd in het volgende InfraOPS Gitlab project:

https://gitlab.bkwi.nl/bkwi/beheer/infraops/ontwikkeltooling/gitlab-ci

Hierin staat ook uitgebreide documentatie over de templates.

De templates staan in deze folder in de repository:

https://gitlab.bkwi.nl/bkwi/beheer/infraops/ontwikkeltooling/gitlab-ci/-/tree/master/gitlab-ci-yml

De keuze van de template bepaalt de configuratie die je verder nog kan doen.

template = microservice-deploy.yml


.gitlab-ci.yml

```git
    include:
      - project: "bkwi/beheer/infraops/ontwikkeltooling/gitlab-ci"
        file: "gitlab-ci-yml/microservice-deploy.yml"
    
    variables:
      SUPPORT_LATEST_TAG: "true"
      SKIP_BDD: "false"
      SKIP_SERENITY: "false"

```

kubernetes/base/bkwi.properties

```git

	tier=edge-inbound
```

kubernetes/base/kustomization.yml

```git
    bases:
      - git@gitlab.bkwi.nl:bkwi/beheer/infraops/ontwikkeltooling/gitlab-ci.git/kustomize-base
    configMapGenerator:
      - behavior: merge
        env: bkwi.properties
        name: 0bkwi-vars
```

**TODO**: Je hebt 3 varianten van de kustomization.yml file (een base en nog een vs versie dacht ik). Beschrijf nog 
wanneer je welke variant nodig hebt. 

[back to main](../README.md) |
[previous](./12_Unit_test_dekking.md) |
[next](./14_Achtergrond_unit_en_integratie_tests.md)

