package control;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
	private Thread tclient = null;
	private Timer timer;
	
	private int proiettili, x, y, pos = 0, k = -1;
	private boolean carica = false;
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
		if(msg.getAzione() == Azione.FINISH) {
			System.out.println("clients:"+clients);
			if(clients > 0)
				stato = Stato.WON;
			else
				stato = Stato.LOST;
		}
	}
	
	/*RICEVE L'AZIONE DALL'INPUT MANAGER/PANNELLO E LA INVIA AL CLIENT*/
	public void inviaAzione(Messaggio msg) {
		if(stato == Stato.PLAY || msg.getAzione() == Azione.DEAD || msg.getAzione() == Azione.FINISH)
			client.inviaOggetto(msg);
	}
	
	/*GESTISCE L'INSERIMENTO DELL'IP DEL SERVER*/
	public void gestioneIp(int key) {
		if(stato == Stato.SCREEN || stato == Stato.WON || stato == Stato.LOST) {
			if(key == KeyEvent.VK_COLON || (key == 46 && k == 16))
				addIp(':');
			if(key == KeyEvent.VK_0  || key == 96)
				addIp('0');
			if(key == KeyEvent.VK_1 || key == 97)
				addIp('1');
			if(key == KeyEvent.VK_2 || key == 98)
				addIp('2');
			if(key == KeyEvent.VK_3 || key == 99)
				addIp('3');
			if(key == KeyEvent.VK_4 || key == 100)
				addIp('4');
			if(key == KeyEvent.VK_5 || key == 101)
				addIp('5');
			if(key == KeyEvent.VK_6 || key == 102)
				addIp('6');
			if(key == KeyEvent.VK_7 || key == 103)
				addIp('7');
			if(key == KeyEvent.VK_8 || key == 104)
				addIp('8');
			if(key == KeyEvent.VK_9 || key == 105)
				addIp('9');
			if((key == KeyEvent.VK_PERIOD || key == 46 || key == 110) && k != 16)
				addIp('.');
			if(key == KeyEvent.VK_BACK_SPACE)
				removeIp();
			if(key == KeyEvent.VK_ENTER) {
				if(stato == Stato.SCREEN) {
					client = new Client(this, this.getIp(), this.getPort());
				}else {
					stato = Stato.SCREEN;
					tclient = null;
				}
			}
			k = key;
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
			pannello.setPlayerStyle(proiettili);
		}
	};
	
	public void run() {
		while(true) {
			//ricarico la posizione degli oggetti dentro all'array
			if(stato != Stato.SCREEN && stato != Stato.WAIT)
				pannello.aggiornaPosizione();
			
			//disegno degli oggetti presenti dentro all'array
			pannello.repaint();
			
			if(stato == Stato.WAIT && tclient == null) {
				tclient = new Thread(client);
				tclient.start();
			}
			
			if(stato == Stato.PLAY || stato == Stato.DEAD) {
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
		pannello.setPlayerStyle(proiettili);
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
		pos = 0;
		while(ip.charAt(pos) != ':' && pos < ip.length()-1)
			pos++;
		return ip.substring(0, pos);
	}
	public int getPort() {
		return Integer.parseInt(ip.substring(pos+1, ip.length()));
	}
	
}
