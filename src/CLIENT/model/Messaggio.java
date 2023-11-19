package CLIENT.model;

import java.io.Serializable;

public class Messaggio implements Serializable{
	
	private int potenza, x; //BULLET
	private int id;
	private Azione azione;
	
	public enum Azione {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		NUP,
		NDOWN,
		NLEFT,
		NRIGHT,
		SHOOT,
		BULLET,
		DEAD,
		FINISH
	}
	
	public Messaggio(int potenza, int x, int id, Azione azione) {
		this.potenza = potenza;
		this.x = x;
		this.id = id;
		this.azione = azione;
	}
	
	/*----- GETTER E SETTER -----*/
	
	public int getPotenza() {
		return potenza;
	}
	public void setPotenza(int potenza) {
		this.potenza = potenza;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Azione getAzione() {
		return azione;
	}
	public void setAzione(Azione azione) {
		this.azione = azione;
	}
	
}