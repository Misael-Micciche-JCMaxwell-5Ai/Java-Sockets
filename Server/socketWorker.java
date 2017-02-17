/*
 * socketWorker.java ha il compito di gestire la connessione al socket da parte di un Client.
 * Elabora il testo ricevuto che in questo caso viene semplicemente mandato indietro con l'aggiunta 
 * di una indicazione che e' il testo che viene dal Server.
 */
import java.net.*;
import java.io.*;

/**
 *
 * @author Prof. Matteo Palitto
 */
class SocketWorker implements Runnable {
  private Socket client;
  private String nick = "";
  private String group = "";
  private BufferedReader in = null;
  private PrintWriter out = null;

    //Constructor: inizializza le variabili
    SocketWorker(Socket client) {
        this.client = client;
    }

    // Questa e' la funzione che viene lanciata quando il nuovo "Thread" viene generato
    public void run(){
        
        try{
          // connessione con il socket per ricevere (in) e mandare(out) il testo
          in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
          System.out.println("Errore: in|out fallito");
          System.exit(-1);
        }
        String line = "";
        //FINCHE' IL NICKNAME NON SI RIPETE 
        controlNick();
        System.out.println("Connesso con: " + nick);
        while(line != null){
          try{
            line = in.readLine();
            //SCRIVENDO NICKNAME SUL CLIENT, STAMPA TUTTA LA LISTA
            switch(line){
                    case "Nickname":
                    {
                        System.out.println(nick + " ha richiesto la lista dei nickname");
                        for(int i = 0; i < ServerTestoMultiThreaded.listaClient.size(); i++)
                        {
                            out.println("Client "+(i+1)+": "+ServerTestoMultiThreaded.listaClient.get(i));
                        }
                    }
                    break;
                    case "New":
                    {
                        System.out.println(nick + " sta creando un nuovo gruppo");
                        out.println("Inserisci il nome del gruppo: ");
                        controlGroup();
                    }
                    break;
                    case "Join":
                    {
                        System.out.println(nick + " si sta aggiungendo ad un gruppo gia' esistente");
                        out.println("Inserisci il nome del gruppo al quale vuoi unirti: ");
                        joinAGroup();
                    }
                    break;
                    case "Groups":
                    {
                        System.out.println(nick + " ha richiesto la lista dei gruppi");
                        for(int i = 0; i < ServerTestoMultiThreaded.listaGroup.size(); i++)
                        {
                            out.println("Group "+(i+1)+": "+ServerTestoMultiThreaded.listaGroup.get(i));
                        }
                    }
                    break;
                    case "Exit":
                    {
                        // ELIMINO IL CLIENT DALLA LISTA
                        ServerTestoMultiThreaded.listaClient.remove(nick);
                        try {
                            out.println("Bye.");
                            client.close();
                            System.out.println("connessione con client: " + nick + " terminata!");
                            return;
                        } catch (IOException e) { System.out.println("Errore connessione con client: " + nick); }
                    }
                    break;
                    case "Help":
                    {
                      System.out.println("Nickname >> Stampa la lista dei nickname nel client");
                      System.out.println("New >> Crea un nuovo gruppo");
                      System.out.println("Join >> Collegamento a un gruppo gia' esistente");
                      System.out.println("Groups >> Stampa la lista dei gruppi");
                      System.out.println("Exit >> Uscita");
                    }
                    break;
                    default:
                    {
                        //scrivi messaggio ricevuto su terminale
                      if(group.equals(""))
                          System.out.println(nick + ">> " + line);
                      else
                      {
                          for(int i = 0; i < ServerTestoMultiThreaded.listaSocket.size(); i++)
                            {
                                if(ServerTestoMultiThreaded.listaSocket.get(i).getGroup().equals(group))
                                {
                                    ServerTestoMultiThreaded.listaSocket.get(i).writeOnClient(line, ServerTestoMultiThreaded.listaSocket.get(i).getNick());
                                }
                            }
                        }
                    }
                    break;
                }
          } catch (IOException e) { System.out.println("lettura da socket fallito");
            System.exit(-1); }
        }
    }
  
  public void controlNick()
    {
        String line = "";
        boolean isNick = false;
        while(!isNick)
        {
            try{
                // LEGGO IL NICKNAME
                line = in.readLine();
                boolean trov = false;
                int i = 0;
                //FINCHE' NON SCORRO TUTTA LA LISTA O NON TROVO IL NICKNAME
                while(trov==false && i < ServerTestoMultiThreaded.listaClient.size())
                {
                    //CONFRONTA IL NICKNAME INSERITO CON LA POSIZIONE i NELLA LISTA 
                    //SE LO TROVA IMPOSTA LA VARIABILE A TRUE, ALTRIMENTI AUMENTA IL CONTATORE
                    if(ServerTestoMultiThreaded.listaClient.get(i).equals(line))
                    {
                        trov = true;
                    } else i++;
                }
                //SE NON VIENE TROVATO IL NICKNAME 
                if(!trov)
                {
                    //IMPOSTA LE VARIABILI
                    nick = line;
                    isNick = true;
                    ServerTestoMultiThreaded.listaClient.add(nick);
                } else {
                    //ALTRIMENTI STAMPA SUL CLIENT
                    out.println("Nickname gia' esistente, inseriscine un altro");
                }            
            } catch(IOException e) { System.out.println("Lettura da socket fallito");
                                 System.exit(-1); }
        }
    }
    
    public void controlGroup()
    {
        boolean isGroup = false;
        String line = "";
        while(!isGroup)
        {
            try{
                // LEGGO IL NICKNAME
                line = in.readLine();
                boolean trov = false;
                int i = 0;
                //FINCHE' NON SCORRO TUTTA LA LISTA O NON TROVO IL NICKNAME
                while(trov==false && i < ServerTestoMultiThreaded.listaGroup.size())
                {
                    //CONFRONTA IL NICKNAME INSERITO CON LA POSIZIONE i NELLA LISTA 
                    //SE LO TROVA IMPOSTA LA VARIABILE A TRUE, ALTRIMENTI AUMENTA IL CONTATORE
                    if(ServerTestoMultiThreaded.listaGroup.get(i).equals(line))
                    {
                        trov = true;
                    } else i++;
                }
                //SE NON VIENE TROVATO IL NICKNAME 
                if(!trov)
                {
                    //IMPOSTA LE VARIABILI
                    group = line;
                    isGroup = true;
                    ServerTestoMultiThreaded.listaGroup.add(group);
                    out.println("Gruppo creato con successo!");
                    System.out.println(nick + " ha creato il gruppo "+group);
                } else {
                    //ALTRIMENTI STAMPA SUL CLIENT
                    out.println("Gruppo gia' esistente, inseriscine un altro");
                }            
            } catch(IOException e) { System.out.println("Lettura da socket fallito");
                                 System.exit(-1); }
          System.out.println(nick + " ha creato il gruppo di nome: " + group);
        }
    }
    
    public void joinAGroup()
    {
        String line = "";
        try{
            line = in.readLine();
        } catch(IOException e){ System.out.println("I|O Error");
                                System.exit(-1); }
        boolean trov = false;
        int i = 0;
        int index = 0;
        while(trov==false && i<ServerTestoMultiThreaded.listaGroup.size())
        {
            if(ServerTestoMultiThreaded.listaGroup.get(i).equals(line))
            {
                trov = true;
                index = i;
            } else i++;
        }
        if(trov)
        {
            group = line;
            out.println("Benvenuto nel gruppo!");
            System.out.println(nick + " si e' unito al gruppo "+group);
        } else {
            out.println("Il gruppo non esiste, prova a crearlo usando il comando <<New>>");
            out.println("I gruppi esistenti sono i seguenti: ");
        }
    }
  
    public void writeOnClient(String line, String nick)
    {
        out.println(line);
        System.out.println(line);
    }
    
    public String getGroup()
    {
        return group;
    }
    
     public String getNick()
    {
        return nick;
    }
}
