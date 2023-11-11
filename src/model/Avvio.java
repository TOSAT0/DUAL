package model;

import java.io.Serializable;

public class Avvio implements Serializable{
	
	private boolean invia;
	
	public Avvio(boolean invia) {
		this.invia = invia;
	}
	
	public void setInvia(boolean invia) {
		this.invia = invia;
	}
	public boolean getInvia() {
		return this.invia;
	}
	
}
