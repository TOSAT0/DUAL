package control;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
	
	private int numClient, port, sq1 = 0, sq2 = 1, c_sq1, c_sq2, c;
	private boolean inizio = false;
	
	private ArrayList<Connessione> clients = new ArrayList<Connessione>();
	
	/*IL SERVER AVVIA LA COMUNICAZIONE*/
	public Server() {
		do {
			System.out.print("# clients number: ");
			numClient = in.nextInt();
			if(numClient < 2 || numClient%2 != 0)
				System.out.println("\tinvalid number");
		}while(numClient < 2 || numClient%2 != 0);
		do {
			System.out.print("# port number: ");
			port = in.nextInt();
			if(port < 10000)
				System.out.println("\tinvalid number");
		}while(port < 10000);
		c_sq1 = numClient/2;
		c_sq2 = numClient/2;
		try {
			server = new ServerSocket(port);
			System.out.println("[SERVER START - "+InetAddress.getLocalHost()+":"+port+"]");
			this.accettaConnessione();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
//-------------------- ALTRI METODI ----------------------------------------//
	
	/*IL SERVER ACCETTA LA CONNESSIONE DEL CLIENT E LA SALVA IN UN ARRAY LIST*/
	public void accettaConnessione() {
		System.out.println();
		try {
			for(c=0; c<numClient; c++) {
				connessione = server.accept();
				System.out.println("Player"+(c+1)+": "+connessione.getInetAddress()+":"+connessione.getPort());
				clients.add(new Connessione(this, connessione, c));
				clients.get(c).inviaOggetto(new Configurazione(numClient));
				new Thread(clients.get(c)).start();
			}
			System.out.println("Server: OK");
			inviaAvvio();
		}catch(IOException e) { e.printStackTrace(); }
		inizio = true;
	}
	
	/*IL SERVER INVIA A TUTTI I CLIENT UN MESSAGGIO DI AVVIO E IL LORO ID*/
	public void inviaAvvio() {
		for(int i=0; i<numClient; i++) {
			clients.get(i).inviaOggetto(new Configurazione(i));
		}
	}
	
	/*INVIA MESSAGGIO AD UN ALTRO THREAD*/
	public void inviaMessagio(Messaggio msg) {
		if(msg.getAzione() == Azione.BULLET || msg.getAzione() == Azione.FINISH) {
			if(msg.getId() == sq1 || msg.getAzione() == Azione.FINISH) {
				System.out.println("[SERVER] Id: "+msg.getId()+" - "+msg.getId()/2+": Action: " + msg.getAzione());
				for(int i=1; i<clients.size(); i+=2)	clients.get(i).inviaOggetto(msg);
			}
			if(msg.getId() == sq2 || msg.getAzione() == Azione.FINISH) {
				System.out.println("[SERVER] Id: "+msg.getId()+" - "+msg.getId()/2+": Action: " + msg.getAzione());
				for(int i=0; i<clients.size(); i+=2)	clients.get(i).inviaOggetto(msg);
			}
			if(msg.getAzione() == Azione.FINISH) {
				System.out.println("[SERVER] Id: "+msg.getId()+" - "+msg.getId()/2+": Action: " + msg.getAzione());
				clients.removeAll(clients);
				inizio = false;
				accettaConnessione();
			}
		}else {
			System.out.println("[SERVER] Id: "+msg.getId()+" - "+msg.getId()/2+": Action: " + msg.getAzione());
			if(msg.getAzione() == Azione.DEAD) {
				if(msg.getId()%2 == 0) {
					c_sq1--;
					sq1+=2;
				}else {
					c_sq2--;
					sq2+=2;
				}
				if(c_sq1 == 0 || c_sq2 == 0) {
					this.inviaMessagio(new Messaggio(-1, -1, -1, Azione.FINISH));
				}
			}
			if(msg.getId()%2 == 0) {
				for(int i=0; i<clients.size(); i+=2) {
					if(msg.getId() != i)
						clients.get(i).inviaOggetto(msg);
				}
			}else {
				for(int i=1; i<clients.size(); i+=2) {
					if(msg.getId() != i)
						clients.get(i).inviaOggetto(msg);
				}
			}
		}
	}
	
	/*GESTISCE LA DISCONNESSIONE DI UN CLIENT*/
	public void clientDisconnesso(int client) {
		if(inizio) {
			this.inviaMessagio(new Messaggio(-1, -1, client, Azione.DEAD));
		}else {
			clients.remove(client);
			this.c--;
		}
		System.out.println("Player"+(client+1)+" disconnected");
	}
	
//-------------------- GETTER E SETTER --------------------//

	public boolean getInizio() {
		return this.inizio;
	}
	
}
