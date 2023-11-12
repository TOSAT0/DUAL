package control;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import model.Avvio;
import model.Messaggio;
import model.Messaggio.Azione;

public class Connessione implements Runnable{
	
	private Socket connessione;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private Server server;
	private Buffer buffer;
	
	private int id;
	
	public Connessione(Server server, Socket connessione, int id) {
		this.server = server;
		this.id = id;
		
		this.buffer = new Buffer(this);
		new Thread(buffer).start();
		
		try {
			this.connessione = connessione;
			output = new ObjectOutputStream(connessione.getOutputStream());
			input = new ObjectInputStream(connessione.getInputStream());
		}catch(IOException e) { e.printStackTrace(); }
	}

//-------------------- RUN --------------------------------------------------//
	
	/*ATTENDE L'ARRIVO DI MESSAGGI DAL CLIENT*/
	public void run() {
		try {
			while(!server.getInizio()) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("Comunicazione pronta");
		
		try {
			output.writeObject(new Avvio((id == 0 || id == 1) ? true : false));
		} catch (IOException e) { e.printStackTrace(); }
		
		Messaggio msg;
		try {
			while(true) {
				msg = (Messaggio) input.readObject();
				System.out.println("msg: "+msg.getAzione());
				
				if(msg.getAzione() == Azione.BULLET) { //1v1 (2v2)
					buffer.setMsg(msg);
				}else { //solo x 2v2
					//invio alla propria squadra
				}
			}
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
	}

//-------------------- ALTRI METODI ------------------------------//
	
	/*INVIA MESSAGGI AL CLIENT*/
	public void inviaOggetto(Messaggio msg) {
		try {
			output.writeObject(msg);
		} catch (IOException e) { e.printStackTrace(); }
	}
}
