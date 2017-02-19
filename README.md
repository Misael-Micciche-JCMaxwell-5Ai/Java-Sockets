
# Java-Sockets
Concetti e realizzazione di una semplice connessione Client/Server usando Sockets in Java
```
Obitettivo del progetto:
inviare linee di testo da un programma (Client)... --> ad un altro (Server) anche eventualmente remoto.
```

## Istruzioni
Per replicare il progetto sul vostro computer personale. Potrete apportare modifiche ed eventualmente fonderle con il progetto principale, o in alcuni casi creare un progetto proprio.

### Prerequisiti
Java SDK (Software Development Kit) - programma per la compilazione (javac) da codice in Java a ByteCode. L'installazione comprende anche Java JRE (Java Runtime Envirorment) che fornisce la Virtual Machine (VM) su cui far eseguire il ByteCode.
```
E' suggerito anche l'utilizzo di NetBeans IDE (Integrated Development Envirorment)
```

### Installazione
Scarica i files del progetto cliccando "Clone/Download"
* in NetBeans crea due nuovi progetti "Java-Sockets-Client" e "Java-Sockets-Server"
* estrai i files e spostali nella sub-directory "src" dei rispettivi progetti (es. Documents\NetBeansProjects\Java-Sockets-Server\src)

### Compilazione
Per prima cosa compiliamo tutte le classi del progetto, sulla finestra del Server scriviamo i comandi:
```
cd Documents\NetBeansProjects\Java-Socket-Server\src>javac SocketWorker.java
cd Documents\NetBeansProjects\Java-Socket-Server\src>javac ServerTestMultiThreaded.java
```
Sulla finestra del Client scriviamo invece:
```
cd Documents\NetBeansProjects\Java-Socket-Client\src>javac listener.java
cd Documents\NetBeansProjects\Java-Socket-Client\src>javac ClientTesto.java
```
**Attenzione** Se la compilazione non funziona, potrebbe essere un problema d'impostazione delle ```variabili d'ambiente```! Se è il vostro caso, seguite le istruzioni qui sotto:<br>

Su ```Prorietà del sistema``` portarsi sul tab ```Avanzate```. Premere il pulsante ```Variabili d'ambiente..```.
Sulla variabile d'ambiente ```Path``` copiare, (al fondo degli indirizzi già copiati), l'indirizzo in cui si trova la bin relavita alla vostra versione della jdk di java.
L'indirizzo sul host su cui è stato eseguito e sviluppato il progetto è: ```C:\Program Files\Java\jdk1.8.0_91\bin```

## Uso
Dal terminale del Server:
```
java ServerTestoMultiThreaded <server port>
```
Da un terminale Client:
```
java clientTesto <host> <server port>
dove:
* host puo' essere espresso sia in forma numerica (es. 127.0.0.1) che in forma alfanumerica (es. www.nomeSito.com)
```
**Nota**: posso collegarmi al server con quanti Clients desidero, sia su stesso computer che da terminali su computer diversi, purchè abbiano diversi nickname

## Comandi implementati

Visualizzare elenco utenti connessi da Client:
```
<Nickname>
```
Creare un nuovo gruppo:
```
<New>
```
Unirsi ad un gruppo già esistente
```
<Join>        
```
Visualizzare elenco gruppi creati
```
<Groups>
```
Visualizzare l'elenco dei comandi disponibili
```
<Help>
```
Abbandonare il gruppo
```
<Quit>
```
Scollegare il client:
```
<Exit>
```

### Esempio usando stesso computer sia per eseguire Server che multipli Clients
Da finestra di comando Server esguire il Server:
```
cd Documents\NetBeansProjects\Java-Sockets-Server\src
java ServerTestoMultiThreaded 1234
```
Da finestra di comando Client eseguire il ```primo``` Client
```
cd Documents\NetBeansProjects\Java-Sockets-Client\src
java client-Testo localhost 1234
```
Da nuova finestra di comando Client esguire il ```secondo``` Client
```
cd Documents\NetBeansProjects\Java-Sockets-Client\src
java client-Testo localhost 1234
```
## Diventa un collaboratore
Questa e' la lista delle funzionalità del progetto:
```
1. Group Chat 
  1.1. Possibilita' di iniziare una o piu' "group chat" fornendo per ogni chat il "Soggetto della discussione"
  1.2. Possibilita' di invitare uno o piu' utenti conessi a entrare nella "goup chat"
  1.3. Possibilita' di uscire dalla "group chat"
```
 
## Licenza
OpenSource

## Ringraziamenti
Al professor Matteo Palitto
