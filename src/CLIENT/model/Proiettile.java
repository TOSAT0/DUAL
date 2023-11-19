package CLIENT.model;

import java.awt.image.BufferedImage;

public class Proiettile extends Oggetto{
	
	private int potenza;
	
	public Proiettile(double x, double y, double dx, double dy, BufferedImage style, int potenza) {
		super(x, y, dx, dy, style);
		this.potenza = potenza;
	}
	
	public void setPotenza(int potenza) {
		this.potenza = potenza;
	}
	
	public int getPotenza() {
		return this.potenza;
	}

}
