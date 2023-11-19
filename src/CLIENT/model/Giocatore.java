package CLIENT.model;

import java.awt.image.BufferedImage;

public class Giocatore extends Oggetto{
	
	private int vita;
	
	public Giocatore(double x, double y, double dx, double dy, BufferedImage style) {
		super(x, y, dx, dy, style);
		this.vita = 10;
	}
	
	public int getVita() {
		return this.vita;
	}
	
	public void setVita(int vita) {
		this.vita = vita;
	}
	
	public boolean isVivo() {
		if(vita == 0)
			return false;
		return true;
	}
	
}
