package CLIENT.model;

import java.io.Serializable;

public class Configurazione implements Serializable{
	
	private int num;
	
	public Configurazione(int num) {
		this.num = num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	public int getNum() {
		return this.num;
	}
	
}
