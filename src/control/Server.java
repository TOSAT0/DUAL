package control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import model.Oggetto;

public class Server implements Runnable{
	
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
			if(numClient < 2 || numClient > 4 || numClient%2 != 0) {
				System.out.println("Numero non valido");
			}
		}while(numClient < 2 || numClient > 4 || numClient%2 != 0);
		
		//avvio il server
		try {
			server = new ServerSocket(5000);
			System.out.println("Server attivo\n");
			this.run();
		}catch(IOException e) { e.printStackTrace(); }
		
	}
	
	public void run() {
		//faccio connettere il numero di client
		try {
			for(int i=0; i<numClient; i++) {
				connessione = server.accept();
				new Connessione(this,connessione,i);
				System.out.println("Client: "+connessione);
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
