package control;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.time.chrono.MinguoEra;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import model.Messaggio;
import model.Messaggio.Azione;
import view.CaricaImmagine;
import view.Pannello;

public class GameEngine implements Runnable{
	public static double P = (Toolkit.getDefaultToolkit().getScreenSize().getHeight())/1080;
	
	public static int height = (int)(1080*0.8*P), width = (int)(1920*0.8*P), id, clients = 0;
	private int proiettili, x, y;
	
	private InputManager inputManager;
	private Pannello pannello;
	private Client client;
	private Thread tclient;
	private Timer timer;
	
	private boolean carica = false, inizio = false;
	
	public GameEngine() {
		inputManager = new InputManager(this);
		client = new Client(this);
		tclient = new Thread(client);
		tclient.start();
		while(clients == 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
		pannello = new Pannello(this, height, width);
		
		proiettili = 10;
		
		//posizione in cui collocare la finestra
		x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width)/2;
		y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height)/2;
		
		//Creazione della finestra
		JFrame frame = new JFrame("DUAL");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.add(pannello);
		frame.pack();
		frame.setLocation(x, y);
		frame.addKeyListener(inputManager);
		
		this.run();
	}
	
//-------------------- METODI GENERICI ----------------------------------------//
	
	/*RICEVE L'AZIONE DELL'INPUT MANAGER/SERVER E LA ELABORA*/
	public void eseguiAzione(Messaggio msg) { //id - 0: azione proveniente dall'input - 1: azione proveniente dal server
		if(msg.getAzione() == Azione.UP)
			pannello.up(msg.getId()/2);
		if(msg.getAzione() == Azione.DOWN)
			pannello.down(msg.getId()/2);
		if(msg.getAzione() == Azione.LEFT)
			pannello.left(msg.getId()/2);
		if(msg.getAzione() == Azione.RIGHT)
			pannello.right(msg.getId()/2);
		if(msg.getAzione() == Azione.NUP)
			pannello.nup(msg.getId()/2);
		if(msg.getAzione() == Azione.NDOWN)
			pannello.ndown(msg.getId()/2);
		if(msg.getAzione() == Azione.NLEFT)
			pannello.nleft(msg.getId()/2);
		if(msg.getAzione() == Azione.NRIGHT)
			pannello.nright(msg.getId()/2);
		if(msg.getAzione() == Azione.SHOOT)
			pannello.shoot(msg.getPotenza(), msg.getId()/2);
		if(msg.getAzione() == Azione.BULLET)
			pannello.bullet(msg.getPotenza(), msg.getX());
	}
	
	/*RICEVE L'AZIONE DALL'INPUT MANAGER/PANNELLO E LA INVIA AL CLIENT*/
	public void inviaAzione(Messaggio msg) {
		client.inviaOggetto(msg);
	}
	
	/*INCREMENTA PROGRESSIVAMENTE IL NUMERO DI PROIETTILI*/
	ActionListener ricarica = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			proiettili++;
			pannello.setPlayer1Style(proiettili);
			System.out.println("+p: "+proiettili);
		}
	};
	
	public void run() {
		//fino a quanto non sono connessi tutti i client
		while(!inizio) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		while(true) {
			//ricarico la posizione degli oggetti dentro all'array
			pannello.aggiornaPosizione();
			
			//disegno degli oggetti presenti dentro all'array
			pannello.repaint();
			
			//gestione della ricarica dei proiettili
			if(proiettili < 10) {
				if(inputManager.spazio()) {
					if(carica) {
						timer.stop();
						carica = false;
					}
				}else {
					if(!carica) {
						timer = new Timer(500, ricarica);
						timer.start();
						carica = true;
					}
				}
			}else {
				if(carica) {
					timer.stop();
					carica = false;
				}
			}
			
			//attendo 50 millisecondi prima di ripetere l'operazione
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
//-------------------- GETTER E SETTER --------------------*/
	
	public void setProiettili(int p) {
		this.proiettili = p;
		pannello.setPlayer1Style(proiettili);
		System.out.println("-p: "+p);
	}
	public int getProiettili() {
		return this.proiettili;
	}
	
	public void setInizio(boolean inizio) {
		this.inizio = inizio;
	}
	public boolean getInizio() {
		return this.inizio;
	}
	
}
