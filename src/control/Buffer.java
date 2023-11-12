package control;

import model.Messaggio;

public class Buffer implements Runnable {
	
	private Messaggio msg;
	private Connessione connessione; 
	
	public Buffer(Connessione connessione) {
		this.connessione = connessione;
		this.msg = new Messaggio(-1,-1,-1,null);
	}
	
	public void run() {
		System.out.println("wait");
		synchronized (msg) {
			try {
				msg.wait();
			}catch (Exception e) { e.printStackTrace(); }
			//connessione.inviaOggetto(msg);
			System.out.println("no wait");
		}
	}
	
	public void setMsg(Messaggio msg) {
		this.msg = msg;
		System.out.println("msg_buffer: "+msg.getAzione());
		try {
			synchronized (msg) {
				msg.notifyAll();
			}
		}catch (Exception e) { e.printStackTrace(); }
		System.out.println("boh");
	}
	
}
