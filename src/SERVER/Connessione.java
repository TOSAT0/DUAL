package SERVER;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import CLIENT.model.*;

public class Connessione implements Runnable{
	
	private Socket connessione;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private Server server;
	
	private int id;
	private boolean vivo = true, run = true;
	
	public Connessione(Server server, Socket connessione, int id) {
		this.server = server;
		this.id = id;
		
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
			while(run) {
				msg = (Messaggio) input.readObject();
				server.inviaMessagio(msg);
			}
		} catch (IOException | ClassNotFoundException e) {
			server.clientDisconnesso(id);
		}
	}

//-------------------- ALTRI METODI ------------------------------//
	
	/*INVIA MESSAGGI AL CLIENT*/
	public void inviaOggetto(Object o) {
		try {
			output.writeObject(o);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
//-------------------- GETTER E SETTER ------------------------------//
	
	public Socket getConnessione() {
		return this.connessione;
	}
	
	public void stopThread() {
	    run = false;
	    try {
	        input.close();
	        output.close();
	        connessione.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
