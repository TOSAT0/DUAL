package model;

import java.io.Serializable;

public class Avvio implements Serializable{
	
	private int id, n;
	
	public Avvio(int id, int n) {
		this.id = id;
		this.n = n;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	
	public void setN(int n) {
		this.n = n;
	}
	public int getN() {
		return this.n;
	}
	
}
