package model;

import java.awt.image.BufferedImage;

public class Giocatore extends Oggetto{
	
	public int vita;
	
	public Giocatore(double x, double y, double dx, double dy, BufferedImage style) {
		super(x, y, dx, dy, style);
		this.vita = 100;
	}
	
}
