/*
 * ServerTesto MultiThreaded.java Server che attende per richieste di connessioni da Clients
 * e li gestisce in modo contemporaneo generando un socket "worker" per ogni connessione.
 * 
 */

/**
 *
 * @author De Santis Veronica
 * @author Checchia Mirko
 * @author Miccichè Misael
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class ServerTestoMultiThreaded {
    //DICHIARAZIONE LISTA CONTENENTE IL NOME DEGLI UTENTI
    static List<String> listaClient = new ArrayList();
    static List<String> listaGroup = new ArrayList();
    static List<SocketWorker> listaSocket = new ArrayList(); 
    public static void main(String[] args) {
        //SE LA STRINGA NON E' UNIVOCA 
        if (args.length != 1) {
            //STAMPA L'USO CORRETTO DEL COMANDO
            System.out.println("Uso: java ServerTestoMultithreaded <Porta Server>");
            return;
        }
        //SE E' UNIVOCA LEGGE IL NUMERO DI PORTA INSERITO
        int portNumber = Integer.parseInt(args[0]);

        try{
            //CREA L'OGGETTO SERVER DI TIPO SERVER SOCKET PASSANDOGLI IL NUMERO DI PORTA
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("Server di Testo in esecuzione...  (CTRL-C quits)\n");
            
            //CICLO INFINITO
            while(true){
                SocketWorker w;
                try {
                    //server.accept returns a client connection
                    w = new SocketWorker(server.accept());
                    Thread t = new Thread(w);
                    t.start();
                    listaSocket.add(w);
                } catch (IOException e) {
                    System.out.println("Connessione NON riuscita con client: ");
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            System.out.println("Error! Porta: " + portNumber + " non disponibile");
            System.exit(-1);
        }

        
    }
}
