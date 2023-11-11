package control;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import model.Avvio;
import model.Messaggio;
import model.Oggetto;
import model.Messaggio.Azione;

public class Connessione implements Runnable{
	
	private Socket connessione;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private Server server;
	
	private int id;
	
	public Connessione(Server server, Socket connessione, int id) {
		this.server = server;
		this.id = id;
		
		try {
			this.connessione = connessione;
			output = new ObjectOutputStream(connessione.getOutputStream());
			input = new ObjectInputStream(connessione.getInputStream());
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	public void run() {
		//attendo che al server si connettano tutti i client
		try {
			while(!server.getInizio()) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("Comunicazione pronta");
		
		//invio l'ok al client
		try {
			output.writeObject(new Avvio((id == 0 || id == 1) ? true : false));
		} catch (IOException e) { e.printStackTrace(); }
		
		/*Messaggio msg;
		try {
			while(true) {
				//riceve l'azione da eseguire
				msg = (Messaggio) input.readObject();
				
				//if(msg.getAzione() == Azione.BULLET)
			}
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }*/
	}
	
	/*----- GETTER E SETTER -----*/
	
	

}
