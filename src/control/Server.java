package control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import model.Oggetto;

public class Server {
	
	private ServerSocket server;
	private Socket connessione;
	private Scanner in = new Scanner(System.in);
	
	private int numClient;
	private boolean inizio = false;
	
	public Server() {
		//imposto il numero di client che si possono connettere
		do {
			System.out.print("Numero di client: ");
			numClient = in.nextInt();
			if(numClient != 2 && numClient != 4) {
				System.out.println("Numero non valido");
			}
		}while(numClient != 2 && numClient != 4);
		
		//avvio il server
		try {
			server = new ServerSocket(10000, 5);
			System.out.println("Server attivo\n");
			this.connessioni();
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	public void connessioni() {
		//faccio connettere il numero di client indicato
		try {
			for(int i=0; i<numClient; i++) {
				System.out.println("test");
				connessione = server.accept();
				System.out.println("Player"+(i+1)+": "+connessione.getInetAddress()+":"+connessione.getPort()+"\n");
				new Thread(new Connessione(this,connessione,i)).start();
			}
			System.out.println("Server: OK");
			inizio = true;
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	/*----- GETTER E SETTER -----*/
	
	public boolean getInizio() {
		return this.inizio;
	}
	
}
