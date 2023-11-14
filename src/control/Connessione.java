package control;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import model.Messaggio;
import model.Messaggio.Azione;

public class Connessione implements Runnable{
	
	private Socket connessione;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private Server server;
	
	public Connessione(Server server, Socket connessione) {
		this.server = server;
		
		try {
			this.connessione = connessione;
			output = new ObjectOutputStream(connessione.getOutputStream());
			input = new ObjectInputStream(connessione.getInputStream());
		}catch(IOException e) { e.printStackTrace(); }
	}

//-------------------- RUN --------------------------------------------------//
	
	/*ATTENDE L'ARRIVO DI MESSAGGI DAL CLIENT*/
	public void run() {
		Messaggio msg;
		try {
			while(true) {
				msg = (Messaggio) input.readObject();
				server.inviaMessagio(msg);
			}
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
	}

//-------------------- ALTRI METODI ------------------------------//
	
	/*INVIA MESSAGGI AL CLIENT*/
	public void inviaOggetto(Object o) {
		try {
			output.writeObject(o);
		} catch (IOException e) { e.printStackTrace(); }
	}
}
