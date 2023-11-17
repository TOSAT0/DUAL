package control;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import model.Configurazione;
import model.Messaggio;
import model.Oggetto;
import model.Stato;
import view.Pannello;
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
	public Client(GameEngine engine, String ip, int port) {
		this.engine = engine;
		
		try {
			System.out.println("ip giusto");
			/*if(ip.equals("0.0.0.0"))
				ip = InetAddress.getLocalHost();*/
			connessione = new Socket(ip, port);
			System.out.println("Connessione: "+connessione); // <-TODO: ELIMINARE
			
			output = new ObjectOutputStream(connessione.getOutputStream());
			input = new ObjectInputStream(connessione.getInputStream());
		}catch(IOException e) {
			System.out.println("ip sbagliato");
			engine.setTryIp(false);
		}
	}
	
//-------------------- RUN --------------------------------------------------//
	
	/*ATTENDE L'ARRIVO DI MESSAGGI DAL SERVER*/
	public void run() {
		try {
			Configurazione config = (Configurazione) input.readObject();
			GameEngine.clients = config.getNum();
			engine.inizializzaArrayGiocatori();
		} catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
		
		try {
			Configurazione config = (Configurazione) input.readObject();
			engine.stato = Stato.START;
			GameEngine.id = config.getNum();
			System.out.println("id: "+GameEngine.id);
		} catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
		
		Messaggio msg;
		try {
			while(true) {
				msg = (Messaggio) input.readObject();
				engine.eseguiAzione(msg);
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
