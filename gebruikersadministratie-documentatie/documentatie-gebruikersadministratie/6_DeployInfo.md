# Deployment informatie en kubernetes

[[_TOC_]]

## Introductie

De Gitlab piplines van de backend en frontend deployen naar development en test. 
Beide applicaties draaien zowel voor O als T in kubernetes in het rancher kluster.

## Kubernetes Shell configuratie

Met  het kubectl commando communiceer je met Kubernetes. Op de bkwi machines is de k alias beschikbaar voor het kubectl commando.
Tevens is kubectl autocomplete geconfigureerd. Dit kan je testen dmv van:

```shell
k --con TAB
```

als het goed is krijg je nu 

```shell
k --context 
```

als autocomplete

Bij kubectl moet je altijd opgeven welke context je wil gebruiken. De gebruikersadministratie applicaties zitten in de context dxc-am3-bto.

```shell
k --context dx TAB
```

je krijgt nu 

```shell
k --context dxc-am3-bto
```

Het kubectl commando kan geconfigureerd worden via bestanden in de folder

```shell
~/.kube
```

In de config file kan je o.a. opgeven wat je default context is. 

### Opvragen van de contexts waar je toegang toe hebt

```shell
$ kubectl config get-contexts
```

### Opvragen van de namespaces binnen je context

```shell
$ kubectl --context dxc-am3-bto get ns
```

De namespaces van de gebruikersadministratie applicatie zijn

- services-inkijk-development
- services-inkijk-test

Ook op de namespaces heb je autocomplete. Dus als je via je -n <namespace> de namespace wil opgeven maak dan gebruik van autocomplete.

### Opvragen van de pods in een namespace

```shell
$ k --context dxc-am3-bto -n services-inkijk-development get pods
```

```shell
mbochove@bkwi-c02d90ryml87:~$ k --context dxc-am3-bto -n services-inkijk-development get pods
NAME                                                              READY   STATUS    RESTARTS   AGE
database-suwinetinkijk-6794df4969-22vqc                           2/2     Running   8          52d
database-useradmin-589b468cb8-kdz77                               2/2     Running   10         52d
gebruikers-administratie-backend-springboot-aeb-1157-7dffcdhz24   2/2     Running   0          4d
gebruikers-administratie-backend-springboot-aeb-1166-76bc5zksmb   2/2     Running   8          7d1h
gebruikers-administratie-frontend-angular-aeb-1157-58d6d946rgfg   2/2     Running   0          83m
samenwerkingsverband-api-v001-aeb-885-7c845f7c69-tppqz            2/2     Running   47         52d
m
```

Meer info
- https://kubernetes.io/docs/tasks/tools/included/optional-kubectl-configs-bash-mac/
- https://kubernetes.io/docs/tasks/tools/included/optional-kubectl-configs-zsh/
- https://kubernetes.io/docs/reference/kubectl/overview/

## Troubleshooting bij falende pod

Soms gebeurt het dat een pod niet goed start nadat hij gedeployed is door Gitlab. De pod krijgt dan bijvoorbeeld de status **_CrashLoopBackOff_** of **_ImagePullBackOff_**.
Hoe kom je er nou achter wat er mis is met een dergelijke pod? Begin met het opvragen van de pods:

```shell
$ k --context dxc-am3-bto -n services-inkijk-development get pods
```

Soms helpt het om de yaml file van een specifieke pod op te vragen. Gebruik de name van de pod uit het vorige commando

```shell
kubectl --context dxc-am3-bto -n services-inkijk-development get pods gebruikers-administratie-frontend-angular-aeb-1157-58d6d946rgfg -o yaml
```

Of gebruik maken van het 'describe' commando van een pod. Gebruik de name van de pod uit het vorige commando

```shell
$ k --context dxc-am3-bto -n services-inkijk-development describe pod gebruikers-administratie-backend-springboot-aeb-1166-6dc4bbgw2k
```



Wat nog beter werkt is het opvragen van de log file van de pod

```shell
$ kubectl --context dxc-am3-bto -n services-inkijk-development logs gebruikers-administratie-frontend-angular-aeb-1157-58d6d946rgfg

error: a container name must be specified for pod gebruikers-administratie-frontend-angular-aeb-1157-58d6d946rgfg, choose one of: [gebruikers-administratie-frontend-angular istio-proxy] or one of the init containers: [istio-init]
```
Met -c kan je de naam van de container opgeven:

```shell
$ kubectl --context dxc-am3-bto -n services-inkijk-development logs gebruikers-administratie-frontend-angular-aeb-1157-58d6d946rgfg -c gebruikers-administratie-frontend-angular
```

## Applicatie openen in browser en port forwarding

Als de applicatie eenmaal met succes gedeployed is, dan wil je hem kunnen bekijken. Indien er nog geen specifieke dns name is voor de applicatie, dan moet je d.m.v. portforwarding de applicatie benaderen.
De frontend applicatie draait op development onder port 8080. Met het volgende commando forwarden we de lokale port 8082 naar de 8080 port van de frontend pod:

```shell
$ k --context dxc-am3-bto -n services-inkijk-development port-forward gebruikers-administratie-frontend-angular-aeb-1157-58d6d946rgfg 8082:8080
```

We kunnen nu de applicatie bekijken via http://localhost:8082/ 

## Overige handige kubernetes commandos

### Open een interactieve shell naar de pod, zodat je vanuit de pod commando's kan geven

```shell
$ k --context dxc-am3-bto -n services-inkijk-development exec --stdin --tty gebruikers-administratie-backend-springboot-aeb-1157-7dffcdhz24 -- /bin/sh
```

### tail -f op de logs van een pod

```shell
$ k --context dxc-am3-bto -n services-inkijk-development logs -f gebruikers-administratie-backend-springboot-aeb-1157-7dffcdhz24 -c gebruikers-administratie-backend-springboot
```


### Handige links
- https://kubernetes.io/docs/reference/kubectl/cheatsheet/#interacting-with-running-pods


[back to main](../README.md) |
[previous](./5_DeveloperInfo.md) |
[next](./7_Authenticatie.md)
