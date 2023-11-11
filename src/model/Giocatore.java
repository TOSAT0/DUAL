package model;

import java.awt.image.BufferedImage;

public class Giocatore extends Oggetto{
	
	public int vita;
	
	public Giocatore(double x, double y, BufferedImage style) {
		super(x, y, style);
		this.vita = 100;
	}
	
}
