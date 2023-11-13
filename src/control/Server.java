package control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import model.Messaggio;
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
				clients.add(new Connessione(this, connessione,i));
				new Thread(clients.get(i)).start();
			}
			System.out.println("Server: OK");
			inizio = true;
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	/*INVIA MESSAGGIO AD UN ALTRO THREAD*/
	public void inviaMessagio(Messaggio msg, int id) {
		if(id == 0)
			id = 1;
		else
			id = 0;
		clients.get(id).inviaOggetto(msg);
	}
	
//-------------------- GETTER E SETTER --------------------//
	
	public boolean getInizio() {
		return this.inizio;
	}
	
}
