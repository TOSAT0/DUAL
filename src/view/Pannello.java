package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
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
import model.Stato;
import model.Messaggio.Azione;

public class Pannello extends JPanel{
	
	private GameEngine engine;
	
	private CaricaImmagine immagine = new CaricaImmagine();
	private CaricaFont font = new CaricaFont();
	
	private ArrayList<Giocatore> giocatori;
	private ArrayList<Proiettile> proiettili = new ArrayList<Proiettile>();
	private ArrayList<Proiettile> proiettili_nemici = new ArrayList<Proiettile>();
	
	private BufferedImage[] giocatoreStyle;
	private BufferedImage proiettileSquadraStyle;
	private BufferedImage proiettileNemicoStyle;
	private BufferedImage vuoto;
	private BufferedImage start_screen;
	
	private int frame = 0;
	
	public Pannello(GameEngine engine, int height, int width) {
		this.engine = engine;
		
        setPreferredSize(new Dimension(width, height));
        giocatoreStyle = getGiocatoreStyle("player-");
        proiettileSquadraStyle = immagine.immagine("proiettile-squadra");
        proiettileNemicoStyle = immagine.immagine("proiettile-nemico");
        vuoto = immagine.immagine("vuoto");
        start_screen = immagine.immagine("start_screen");
	}

//---------- METODI ------------------------------//
	
	public void inizializzaArrayGiocatori() {
		giocatori = new ArrayList<Giocatore>();
		for(int i=0; i < engine.clients; i++)
			giocatori.add(new Giocatore(GameEngine.width/2, GameEngine.height/2, 15*15*GameEngine.P, 11*15*GameEngine.P, giocatoreStyle[0]));
		this.setPlayerStyle(10);
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setFont(font.getFont(75));
        
        if(engine.stato == Stato.SCREEN || engine.stato == Stato.WAIT) {
        	g2D.drawImage(start_screen, 0, 0, engine.width, engine.height, null);
        	g2D.drawString("Enter IP and Port:"+engine.getTotIp(), 50, 100);
        	if(engine.stato == Stato.WAIT) {
        		if(frame >= 0 && frame < 10)
        			g2D.drawString("Waiting for players", 50, 200);
        		if(frame >= 10 && frame < 20)
        			g2D.drawString("Waiting for players.", 50, 200);
        		if(frame >= 20 && frame < 30)
        			g2D.drawString("Waiting for players..", 50, 200);
        		if(frame >= 30 && frame < 40)
        			g2D.drawString("Waiting for players...", 50, 200);
        		if(frame >= 40)
        			frame = 0;
        		frame++;
        	}
        }
        if(engine.stato == Stato.WON){
        	g2D.drawString("You WON", 50, 100);
        }
        if(engine.stato == Stato.LOST) {
        	g2D.drawString("You LOST", 50, 100);
        }
        if(engine.stato == Stato.WON || engine.stato == Stato.LOST) {
        	if(frame >= 0 && frame < 10)
        		g2D.drawString("Press enter to play again", 50, 200);
        	if(frame >= 10 && frame < 20)
        		g2D.drawString("Press enter to play again.", 50, 200);
        	if(frame >= 20 && frame < 30)
        		g2D.drawString("Press enter to play again..", 50, 200);
        	if(frame >= 30 && frame < 40)
        		g2D.drawString("Press enter to play again...", 50, 200);
        	if(frame >= 40)
        		frame = 0;
        	frame++;
        }
        if(engine.stato == Stato.PLAY || engine.stato == Stato.DEAD){
            for(Giocatore og : giocatori) {
                g2D.drawImage(og.getStyle(), (int)og.getX(), (int)og.getY(), (int)og.getDx(), (int)og.getDy(), null);
            }
            for(Proiettile og : proiettili) {
                g2D.drawImage(og.getStyle(), (int)og.getX(), (int)og.getY(), (int)og.getDx(), (int)og.getDy(), null);
            }
            for(Proiettile og : proiettili_nemici) {
                g2D.drawImage(og.getStyle(), (int)og.getX(), (int)og.getY(), (int)og.getDx(), (int)og.getDy(), null);
            }
            
            g2D.drawString(String.valueOf(giocatori.get(GameEngine.id/2).getVita()*5)+"%", 25, 75);
        }
    }
	
	public void aggiornaPosizione() {
		boolean controllo;
		ArrayList<Proiettile> elimina = new ArrayList<Proiettile>();
		for(Giocatore g : giocatori) {
            g.aggiornaPosizione();
        }
        for(Proiettile p : proiettili) {
            controllo = p.aggiornaPosizione();
            if(controllo) {
        		elimina.add(p);
    			engine.inviaAzione(new Messaggio(p.getPotenza(), (int) p.getX(), GameEngine.id, Azione.BULLET));
        	}
        }
        for(Proiettile p : proiettili_nemici) {
            controllo = p.aggiornaPosizione();
            if(controllo) {
        		elimina.add(p);
        	}
        }
        for(Proiettile p : elimina) {
        	proiettili.remove(p);
        	proiettili_nemici.remove(p);
        }
	}
	
	public void controlloHitbox() {
		ArrayList<Proiettile> elimina = new ArrayList<Proiettile>();
		for(Proiettile p : proiettili_nemici) {
			for(Giocatore g : giocatori) {
				if(p.getHitbox().intersects(g.getHitbox())) {
					if(g.isVivo())
						elimina.add(p);
					if(/*G DEVE ESSERE IL GIOCATORE EFFETTIVO, NON UNO A CASO*/) {
						if(g.getVita() - p.getPotenza() > 0) {
							g.setVita(g.getVita()-p.getPotenza());
						}else {
							g.setVita(0);
							engine.clients--;
							engine.stato = Stato.DEAD;
							engine.eseguiAzione(new Messaggio(-1, -1, GameEngine.id, Azione.DEAD));
							engine.inviaAzione(new Messaggio(-1, -1, GameEngine.id, Azione.DEAD));
						}
					}
				}
			}
		}
		for(Proiettile p : elimina)
			proiettili_nemici.remove(p);
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
		proiettili_nemici.add(new Proiettile(x*GameEngine.P, 0, potenza*16*GameEngine.P, potenza*16*GameEngine.P, proiettileNemicoStyle, potenza));
		proiettili_nemici.get(proiettili_nemici.size()-1).setVelY(2);
	}
	
	//DEAD
	public void dead(int i) {
		giocatori.get(i).setStyle(vuoto);
	}

//---------- STILE DEI GIOCATORI ------------------------------//
	
	public BufferedImage[] getGiocatoreStyle(String nome) {
		BufferedImage[] style = new BufferedImage[11];
		for(int i=0; i<11; i++) {
			style[i] = immagine.immagine(nome+i);
		}
		return style;
	}
	
	public void setPlayerStyle(int i) {
		giocatori.get(GameEngine.id/2).setStyle(giocatoreStyle[i]);
	}
	
}
