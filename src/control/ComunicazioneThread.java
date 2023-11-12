package control;

import model.Messaggio;

public class ComunicazioneThread implements Runnable {
	
	private Messaggio msg;
	private Connessione connessione; 
	
	public ComunicazioneThread(Connessione connessione) {
		this.connessione = connessione;
	}
	
	public void run() {
		while(true) {
			synchronized (msg) {
				try {
					msg.wait();
				}catch (Exception e) { e.printStackTrace(); }
				connessione.inviaOggetto(msg);
			}
		}
	}
	
	public void setMsg(Messaggio msg) {
		this.msg = msg;
		msg.notifyAll();
	}
	
}
