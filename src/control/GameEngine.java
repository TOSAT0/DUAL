package control;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ObjectInputFilter.Status;
import java.time.chrono.MinguoEra;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import model.Messaggio;
import model.Messaggio.Azione;
import model.Stato;
import view.CaricaImmagine;
import view.Pannello;

public class GameEngine implements Runnable{
	public static double P = (Toolkit.getDefaultToolkit().getScreenSize().getHeight())/1080;

	public static Stato stato = Stato.SCREEN;
	public static int height = (int)(1080*0.8*P), width = (int)(1920*0.8*P), id, clients = 0;
	
	private InputManager inputManager;
	private Pannello pannello;
	private Client client;
	private Thread tclient;
	private Timer timer;
	
	private int proiettili, x, y, pos = 0;
	private boolean carica = false, tryIp = false;
	private StringBuilder ip = new StringBuilder("");
	
	public GameEngine() {
		inputManager = new InputManager(this);
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
		
		while(stato == Stato.SCREEN)
			pannello.repaint();
		this.run();
	}
	
//-------------------- METODI GENERICI ----------------------------------------//
	
	/*RICEVE L'AZIONE DELL'INPUT MANAGER/SERVER E LA ELABORA*/
	public void eseguiAzione(Messaggio msg) {
		System.out.println("Id: "+msg.getId()+" - "+msg.getId()/2+": Action: " + msg.getAzione());
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
		if(msg.getAzione() == Azione.DEAD)
			pannello.dead(msg.getId()/2);
	}
	
	/*RICEVE L'AZIONE DALL'INPUT MANAGER/PANNELLO E LA INVIA AL CLIENT*/
	public void inviaAzione(Messaggio msg) {
		if(GameEngine.clients > 2 || msg.getAzione() == Azione.BULLET)
			client.inviaOggetto(msg);
	}
	
	/*GESTISCE L'INSERIMENTO DELL'IP DEL SERVER*/
	public void gestioneIp(int key) {
		if(key == 16)
			addIp(':');
		if(key == 96)
			addIp('0');
		if(key == 97)
			addIp('1');
		if(key == 98)
			addIp('2');
		if(key == 99)
			addIp('3');
		if(key == 100)
			addIp('4');
		if(key == 101)
			addIp('5');
		if(key == 102)
			addIp('6');
		if(key == 103)
			addIp('7');
		if(key == 104)
			addIp('8');
		if(key == 105)
			addIp('9');
		if(key == 110)
			addIp('.');
		if(key == KeyEvent.VK_BACK_SPACE)
			removeIp();
		if(key == KeyEvent.VK_ENTER) {
			client = new Client(this, this.getIp(), this.getPort());
			tclient = new Thread(client);
			tclient.start();
		}
	}
	
	/*INIZIALIZZA L'ARRAY GIOCATORI*/
	public void inizializzaArrayGiocatori(){
		pannello.inizializzaArrayGiocatori();
	}
	
	/*INCREMENTA PROGRESSIVAMENTE IL NUMERO DI PROIETTILI*/
	ActionListener ricarica = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			proiettili++;
			pannello.setPlayer1Style(proiettili);
		}
	};
	
	public void run() {
		while(true) {
			//ricarico la posizione degli oggetti dentro all'array
			pannello.aggiornaPosizione();
			
			//disegno degli oggetti presenti dentro all'array
			pannello.repaint();
			
			//controllo le collisioni
			pannello.controlloHitbox();
			
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
			
			if(clients == 0) {
				client.inviaOggetto(new Messaggio(-1, -1, id, Azione.WIN));
				stato = Stato.END;
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
	}
	public int getProiettili() {
		return this.proiettili;
	}
	
	public StringBuilder getTotIp() {
		return this.ip;
	}
	public void addIp(Character c) {
		 ip.append(c);
	}
	public void removeIp() {
		if(ip.length() != 0)
			ip.deleteCharAt(ip.length()-1);
	}
	
	public String getIp() {
		while(ip.charAt(pos) != ':')
			pos++;
		return ip.substring(0, pos-1);
	}
	public int getPort() {
		return Integer.parseInt(ip.substring(pos+1, ip.length()-1));
	}
	
	public void setTryIp(boolean tryIp) {
		this.tryIp = tryIp;
	}
	
}
