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
	
	public Client(GameEngine engine) {
		this.engine = engine;
		
		try {
			connessione = new Socket(InetAddress.getLocalHost(), 10000);
			output = new ObjectOutputStream(connessione.getOutputStream());
			input = new ObjectInputStream(connessione.getInputStream());
			
			Avvio avvio = (Avvio) input.readObject();
			engine.setInizio(true);
			engine.setInvia(avvio.getInvia());
			System.out.println("Server: OK"); // <-TODO: ELIMINARE
		}catch(IOException | ClassNotFoundException e) { e.printStackTrace(); }
		
		this.run();
	}
	
	public void inviaOggetto(Messaggio msg) {
		/*try {
			output.writeObject(msg);
		} catch (IOException e) { e.printStackTrace(); }*/
	}
	
	public void run() {
		Messaggio msg;
		
		try {
			while(true) {
				System.out.println("prova");
				msg = (Messaggio) input.readObject();
				System.out.println("sleep");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				engine.eseguiAzione(msg,1);
			}
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
	}
	
}
