package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize.Engineering;
import javax.swing.JPanel;

import control.GameEngine;
import model.Giocatore;
import model.Messaggio;
import model.Oggetto;
import model.Proiettile;
import model.Messaggio.Azione;

public class Pannello extends JPanel{
	
	private GameEngine engine;
	
	private CaricaImmagine immagine = new CaricaImmagine();
	
	private ArrayList<Giocatore> giocatori = new ArrayList<Giocatore>();
	private ArrayList<Proiettile> proiettili = new ArrayList<Proiettile>();
	
	private BufferedImage[] giocatore1Style;
	private BufferedImage proiettileSquadraStyle;
	private BufferedImage proiettileNemicoStyle;
	
	public Pannello(GameEngine engine, int height, int width) {
		this.engine = engine;
		
        setPreferredSize(new Dimension(width, height));
        giocatore1Style = getGiocatoreStyle("player1-");
        proiettileSquadraStyle = immagine.immagine("proiettile-squadra");
        proiettileNemicoStyle = immagine.immagine("proiettile-nemico");
        
        inizializzaArrayGiocatori();
	}

//---------- METODI ------------------------------//
	
	public void inizializzaArrayGiocatori() {
		for(int i=0; i < GameEngine.clients/2; i++)
			giocatori.add(new Giocatore(GameEngine.width/2, GameEngine.height/2, 15*15*GameEngine.P, 11*15*GameEngine.P, giocatore1Style[10]));
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g.create();
        
        for(Giocatore og : giocatori) {
            g2D.drawImage(og.getStyle(), (int)og.getX(), (int)og.getY(), (int)og.getDx(), (int)og.getDy(), null);
        }
        for(Proiettile og : proiettili) {
            g2D.drawImage(og.getStyle(), (int)og.getX(), (int)og.getY(), (int)og.getDx(), (int)og.getDy(), null);
        }
    }
	
	public void aggiornaPosizione() {
		int controllo;
		ArrayList<Proiettile> elimina = new ArrayList<Proiettile>();
		for(Giocatore g : giocatori) {
            g.aggiornaPosizione();
        }
        for(Proiettile p : proiettili) {
            controllo = p.aggiornaPosizione();
            if(controllo != 0) {
        		elimina.add(p);
        		if(controllo == 1 && (GameEngine.id == 0 || GameEngine.id == 1))
        			engine.inviaAzione(new Messaggio(p.getPotenza(), (int) p.getX(), GameEngine.id, Azione.BULLET));
        	}
        }
        for(Proiettile p : elimina)
        	proiettili.remove(p);
	}

//---------- AZIONI ------------------------------//
	
	//UP
	public void up(int i) {
		giocatori.get(i).setVelY(-1);
	}
	public void nup(int i) {
		if(giocatori.get(i).getVelY() == -1)
			giocatori.get(i).setVelY(0);
	}
	
	//DOWN
	public void down(int i) {
		giocatori.get(i).setVelY(1);
	}
	public void ndown(int i) {
		if(giocatori.get(i).getVelY() == 1)
			giocatori.get(i).setVelY(0);
	}
	
	//LEFT
	public void left(int i) {
		giocatori.get(i).setVelX(-1);
	}
	public void nleft(int i) {
		if(giocatori.get(i).getVelX() == -1)
			giocatori.get(i).setVelX(0);
	}
	
	//RIGHT
	public void right(int i) {
		giocatori.get(i).setVelX(1);
	}
	public void nright(int i) {
		if(giocatori.get(i).getVelX() == 1)
			giocatori.get(i).setVelX(0);
	}
	
	//SHOOT
	public void shoot(int potenza, int i) {
		proiettili.add(new Proiettile((giocatori.get(i).getX()+15*15/2)-(potenza*8), giocatori.get(i).getY()-(potenza*8), potenza*16*GameEngine.P, potenza*16*GameEngine.P, proiettileSquadraStyle, potenza));
		proiettili.get(proiettili.size()-1).setVelY(-2);
	}
	
	//BULLET
	public void bullet(int potenza, int x) {
		proiettili.add(new Proiettile(x*GameEngine.P, 0, potenza*16*GameEngine.P, potenza*16*GameEngine.P, proiettileNemicoStyle, potenza));
		proiettili.get(proiettili.size()-1).setVelY(2);
	}

//---------- STILE DEI GIOCATORI ------------------------------//
	
	public BufferedImage[] getGiocatoreStyle(String nome) {
		BufferedImage[] style = new BufferedImage[11];
		for(int i=0; i<11; i++) {
			style[i] = immagine.immagine(nome+i);
		}
		return style;
	}
	
	public void setPlayer1Style(int i) {
		giocatori.get(0).setStyle(giocatore1Style[i]);
	}
	
}
