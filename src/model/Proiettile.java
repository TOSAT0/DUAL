package model;

import java.awt.image.BufferedImage;

public class Proiettile extends Oggetto{
	
	private int potenza;
	
	public Proiettile(double x, double y, BufferedImage style, int potenza) {
		super(x, y, style);
		this.potenza = potenza;
	}
	
	public void setPotenza(int potenza) {
		this.potenza = potenza;
	}
	
	public int getPotenza() {
		return this.potenza;
	}

}
