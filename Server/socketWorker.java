/*
 * socketWorker.java ha il compito di gestire la connessione al socket da parte di un Client.
 * Elabora il testo ricevuto che in questo caso viene semplicemente mandato indietro con l'aggiunta 
 * di una indicazione che e' il testo che viene dal Server.
 */
import java.net.*;
import java.io.*;

/**
 *
 * @author De Santis Veronica
 * @author Checchia Mirko
 * @author Miccichè Misael
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
    
    public String getGroup()
    {
        return group;
    }
    
    public String getNick()
    {
        return nick;
    }

    // Metodo per scrivere su client
    public void write(String line)
    {
        out.println(line);
    }
    
    // Questa e' la funzione che viene lanciata quando il nuovo "Thread" viene generato
    public void run(){
        
        try{
          // connessione con il socket per ricevere (in) e mandare (out) il testo
          in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
          System.out.println("Errore: in|out fallito");
          System.exit(-1);
        }
        String line = "";
        // Controlla che l'inserimento del nickanme avvenga correttamente
        controlNick();
        System.out.println("Connesso con: " + nick);
        while(line != null){
          try{
            line = in.readLine();
            switch(line){
                // Stampa la lista di tutti i client presenti
                case "User":
                {
                    System.out.println(nick + " ha richiesto la lista dei client");
                    for(int i = 0; i < ServerTestoMultiThreaded.listaClient.size(); i ++)
                    {
                        out.println("Client "+ (i+1) + "=>" + ServerTestoMultiThreaded.listaClient.get(i));
                    }
                }
                break;
                case "New":
                {
                    System.out.println(nick + " sta creando un nuovo gruppo");
                    newGroup();
                }
                break;
                case "Join":
                {
                    System.out.println(nick + " si sta unendo ad un gruppo");
                    joinGroup();
                }
                break;
                case "Invite":
                {
                    // Creare un metodo a parte
                }
                case "Groups":
                {
                    if(ServerTestoMultiThreaded.listaGroup.isEmpty())
                        out.println("Non sono ancora stati creati gruppi");
                    else{
                        System.out.println(nick + " ha richiesto la lista dei gruppi");
                        for(int i = 0; i < ServerTestoMultiThreaded.listaGroup.size(); i ++)
                        {
                            out.println("Gruppo "+ (i+1) + "=>" + ServerTestoMultiThreaded.listaGroup.get(i));
                        }
                    }
                }
                break;
                case "Quit":
                {
                    System.out.println(nick + " e' uscito dal gruppo "+group);
                    out.println("Sei uscito dal gruppo");
                    notifyOther(nick + " e' uscito dal gruppo");
                    group = "";
                }
                break;
                case "Exit":
                {
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
                    System.out.println(nick + " ha rischiesto la lista di comandi");
                    out.println("User >> Stampa la lista dei nickname nel client");
                    out.println("New >> Crea un nuovo gruppo");
                    out.println("Join >> Collegamento a un gruppo gia' esistente");
                    out.println("Groups >> Stampa la lista dei gruppi");
                    out.println("Help >> Mostra la lista dei comandi utilizzabili");
                    out.println("Exit >> Uscita");
                }
                break;
                default:
                {
                    if(group.equals(""))
                        System.out.println(nick + ">> " + line);
                    else{
                        notifyOther(nick + ">>" + line);
                        System.out.println(nick + ">>" + group + ">>" + line);
                    }
                }
                break;
            }
          } catch (IOException e) { System.out.println("lettura da socket fallito");
            System.exit(-1); }
        }
    }

    // Metodo che controlla che il nickname inserito NON sia già esistente
    public void controlNick(){
        String line = "";
        boolean isNick = false;
        while(!isNick)
        {
            try{
                line = in.readLine();
                boolean trov = false;
                int i = 0;
                while(trov==false && i < ServerTestoMultiThreaded.listaClient.size())
                {
                    if(ServerTestoMultiThreaded.listaClient.get(i).equals(line))
                        trov = true;
                    else
                        i++;
                }
                if(!trov)
                {
                    nick = line;
                    isNick = true;
                    ServerTestoMultiThreaded.listaClient.add(nick);
                } else
                    out.println("Nickname gia' esistente, inseriscine un altro");            
            } catch(IOException e) { System.out.println("Lettura da socket fallito");
                                 System.exit(-1); }
        }
    }

    // Metodo che controlla che il nome del gruppo NON sia già esistente e crea il gruppo
    public void newGroup(){
        String line = "";
        boolean isGroup = false;
        while(!isGroup)
        {
            out.println("Inserisci il nome del gruppo:");
            try{
                line = in.readLine();
                boolean trov = false;
                int i = 0;
                while(trov==false && i < ServerTestoMultiThreaded.listaGroup.size())
                {
                    if(ServerTestoMultiThreaded.listaGroup.get(i).equals(line))
                        trov = true;
                    else
                        i++;
                }
                if(!trov)
                {
                    group = line;
                    isGroup = true;
                    ServerTestoMultiThreaded.listaGroup.add(group);
                    out.println("Gruppo creato con successo!");
                    System.out.println(nick + " ha creato il gruppo "+group);
                } else
                    out.println("Gruppo gia' esistente, inseriscine un altro");         
            } catch(IOException e) { System.out.println("Lettura da socket fallito");
                                 System.exit(-1); }
        }
        
    }

    public void joinGroup(){
        String line = "";
        boolean isJoin = false;
        while(!isJoin)
        {
            out.println("Inserisci il nome del gruppo");
            try{
                line = in.readLine();
                boolean trov = false;
                int i = 0;
                while(trov == false && i < ServerTestoMultiThreaded.listaGroup.size())
                {
                    if(ServerTestoMultiThreaded.listaGroup.get(i).equals(line))
                        trov = true;
                    else
                        i++;
                }
                if(trov)
                {
                    group = line;
                    isJoin = true;
                    out.println("Ti sei unito al gruppo con successo!");
                    System.out.println(nick + " si e' aggiunto al gruppo " + group);
                    notifyOther(nick + " si e' aggiunto al gruppo");
                } else
                    out.println("Il gruppo non e' stato trovato");
            } catch(IOException e) { System.out.println("Lettura da socket fallito");
                                 System.exit(-1); }
        }
    }
    
    public void notifyOther(String whatToWrite){
        for(int i = 0; i < ServerTestoMultiThreaded.listaSocket.size(); i ++)
        {
            if(ServerTestoMultiThreaded.listaSocket.get(i).getGroup().equals(group))
            {
                if(!ServerTestoMultiThreaded.listaSocket.get(i).getNick().equals(nick))
                    ServerTestoMultiThreaded.listaSocket.get(i).write(whatToWrite);
            }
        }
    }
}
