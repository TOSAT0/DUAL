package control;

import model.Messaggio;

public class Buffer {
	
	private Messaggio msg_squadra;
	private Messaggio msg_nemico;
	private boolean squadra;
	private boolean nemico;
	
	public Buffer() {}
	
	public void squadra(Messaggio msg) {
		this.msg_squadra = msg;
		squadra = true;
	}
	
	public void nemico(Messaggio msg) {
		this.msg_nemico = msg;
		nemico = false;
	}
	
	public void setSquadra(boolean squadra) {
		this.squadra = squadra;
	}
	
	public void setNemico(boolean nemico) {
		this.nemico = nemico;
	}
	
}
