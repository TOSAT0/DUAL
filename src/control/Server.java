package control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import model.Configurazione;
import model.Messaggio;
import model.Messaggio.Azione;
import model.Oggetto;

public class Server {
	
	private ServerSocket server;
	private Socket connessione;
	private Scanner in = new Scanner(System.in);
	
	private int numClient;
	private boolean inizio = false;
	
	private ArrayList<Connessione> clients = new ArrayList<Connessione>();
	
	/*IL SERVER AVVIA LA COMUNICAZIONE*/
	public Server() {
		do {
			System.out.print("Numero di client: ");
			numClient = in.nextInt();
			if(numClient != 2 && numClient != 4) {
				System.out.println("Numero non valido");
			}
		}while(numClient != 2 && numClient != 4);
		try {
			server = new ServerSocket(10000, 5);
			System.out.println("Server attivo\n");
			this.accettaConnessione();
		}catch(IOException e) { e.printStackTrace(); }
	}
	
//-------------------- ALTRI METODI ----------------------------------------//
	
	/*IL SERVER ACCETTA LA CONNESSIONE DEL CLIENT E LA SALVA IN UN ARRAY LIST*/
	public void accettaConnessione() {
		try {
			for(int i=0; i<numClient; i++) {
				connessione = server.accept();
				System.out.println("Player"+(i+1)+": "+connessione.getInetAddress()+":"+connessione.getPort());
				clients.add(new Connessione(this, connessione));
				clients.get(i).inviaOggetto(new Configurazione(numClient));
				new Thread(clients.get(i)).start();
			}
			System.out.println("Server: OK");
			inviaAvvio();
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	/*IL SERVER INVIA A TUTTI I CLIENT UN MESSAGGIO DI AVVIO E IL LORO ID*/
	public void inviaAvvio() {
		for(int i=0; i<numClient; i++) {
			clients.get(i).inviaOggetto(new Configurazione(i));
		}
	}
	
	/*INVIA MESSAGGIO AD UN ALTRO THREAD*/
	public void inviaMessagio(Messaggio msg) {
		System.out.println("msg_server: "+msg.getAzione());
		if(msg.getAzione() == Azione.BULLET) {
			if(msg.getId() == 0) {
				for(int i=1; i<clients.size(); i+=2)	clients.get(i).inviaOggetto(msg);
			}
			if(msg.getId() == 1) {
				for(int i=0; i<clients.size(); i+=2)	clients.get(i).inviaOggetto(msg);
			}
		}else {
			if(msg.getId()%2 == 0) {
				for(int i=0; i<clients.size(); i+=2)	clients.get(i).inviaOggetto(msg);
			}else {
				for(int i=1; i<clients.size(); i+=2)	clients.get(i).inviaOggetto(msg);
			}
		}
	}
	
//-------------------- GETTER E SETTER --------------------//
	
	public boolean getInizio() {
		return this.inizio;
	}
	
}
