package control;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import model.Avvio;
import model.Messaggio;
import model.Oggetto;
import model.Messaggio.Azione;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Client implements Runnable{
	
	private Socket connessione;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private GameEngine engine;
	
	/*IL CLIENT SI CONNETTE AL SERVER*/
	public Client(GameEngine engine) {
		this.engine = engine;
		
		try {
			connessione = new Socket(InetAddress.getLocalHost(), 10000);
			System.out.println("Connessione: "+connessione); // <-TODO: ELIMINARE
			
			output = new ObjectOutputStream(connessione.getOutputStream());
			input = new ObjectInputStream(connessione.getInputStream());
		}catch(IOException e) { e.printStackTrace(); }
	}
	
//-------------------- RUN --------------------------------------------------//
	
	/*ATTENDE L'ARRIVO DI MESSAGGI DAL SERVER*/
	public void run() {
		try {
			Avvio avvio = (Avvio) input.readObject();
			engine.setInizio(true);
			engine.setInvia(avvio.getInvia());
			System.out.println("Server: OK"); // <-TODO: ELIMINARE
		} catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
		
		Messaggio msg;
		try {
			while(true) {
				msg = (Messaggio) input.readObject();
				engine.eseguiAzione(msg,1);
			}
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
	}

//-------------------- ALTRI METODI ----------------------------------------//
	
	/*INVIA MESSAGGI AL SERVER*/
	public void inviaOggetto(Messaggio msg) {
		try {
			if(msg.getAzione() == Azione.BULLET)
				msg.setX((int)(msg.getX()/GameEngine.P));
			output.writeObject(msg);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
}
