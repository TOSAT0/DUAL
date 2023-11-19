package CLIENT.model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import CLIENT.control.*;
import CLIENT.view.*;

public class Oggetto {
	
	private BufferedImage style;
	private double x, y, dx, dy;
	private int velX, velY;
	
	public Oggetto(double x, double y, double dx, double dy, BufferedImage style) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.velX = 0;
		this.velY = 0;
		this.style = style;
	}

	public boolean aggiornaPosizione() {
		if(velX == 1) {
			if(x+30 <= GameEngine.width-255)
				x+=30;
			else
				x=GameEngine.width-225;
		}
		if(velX == -1) {
			if(x-30 >= 0)
				x-=30;
			else
				x=0;
		}
		if(velY == 1) {
			if(y+30 <= GameEngine.height-170)
				y+=30;
			else
				y=GameEngine.height-170;
		}
		if(velY == -1) {
			if(y-30 >= 0)
				y-=30;
			else
				y=0;
		}
		if(velY == 2) {
			y+=60;
			if(y>GameEngine.height)
				return true;
		}
		if(velY == -2) {
			y-=60;
			if(y < 0)
				return true;
		}
		return false;
	}
	
//-------------------- GETTER E SETTER --------------------//
	
	public BufferedImage getStyle() {
		return style;
	}

	public void setStyle(BufferedImage style) {
		this.style = style;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}
	
	public Rectangle getHitbox() {
		return new Rectangle((int)x, (int)y, (int)dx, (int)dy);
	}
	
}
