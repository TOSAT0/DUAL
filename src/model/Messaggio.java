package model;

import java.io.Serializable;

public class Messaggio implements Serializable{
	
	private int potenza, x, y; //BULLET
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
		NONE
	}
	
	public Messaggio(int potenza, int x, int y, Azione azione) {
		this.potenza = potenza;
		this.x = x;
		this.y = y;
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
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public Azione getAzione() {
		return azione;
	}
	public void setAzione(Azione azione) {
		this.azione = azione;
	}
	
}