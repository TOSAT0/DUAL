package control;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import model.Messaggio;
import model.Messaggio.Azione;
import model.Stato;

public class InputManager implements KeyListener{
	
	private GameEngine engine;
	private Timer timer;
	private int potenza;
	private boolean spazio = false, up = false, down = false, left = false, right = false;
	
	public InputManager(GameEngine engine) {
		this.engine = engine;
	}
	
	ActionListener carica = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
        	if(engine.getProiettili() > 0) {
            	engine.setProiettili(engine.getProiettili()-1);
            	potenza++;
        	}
        }
    };
	
	public void eseguiAzione(Messaggio msg) {
		if(msg.getAzione() != null && engine.stato != Stato.STOP)
			engine.eseguiAzione(msg);
	}
	
	public void inviaAzione(Messaggio msg) {
		if(msg.getAzione() != null && engine.stato != Stato.STOP)
			engine.inviaAzione(msg);
	}
	
	public boolean spazio() {
		return this.spazio;
	}
	
//-------------------- EVENTI DA TASTIERA --------------------//
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) { //pressione tasto
		int key = e.getKeyCode();
		Azione azione = null;
		
		if(!up) {
			if(key == KeyEvent.VK_UP) {
				azione = Messaggio.Azione.UP;
				up = true;
			}
		}
		if(!down) {
			if(key == KeyEvent.VK_DOWN) {
				azione = Messaggio.Azione.DOWN;
				down = true;
			}
		}
		if(!left) {
			if(key == KeyEvent.VK_LEFT) {
				azione = Messaggio.Azione.LEFT;
				left = true;
			}
		}
		if(!right) {
			if(key == KeyEvent.VK_RIGHT) {
				azione = Messaggio.Azione.RIGHT;
				right = true;
			}
		}
		if(!spazio) {
			if(key == KeyEvent.VK_SPACE) {
				spazio = true;
				potenza = 0;
				if(engine.getProiettili() > 0) {
					engine.setProiettili(engine.getProiettili()-1);
					potenza = 1;
				}
				timer = new Timer(500, carica);
				timer.start();
			}
		}
		
		eseguiAzione(new Messaggio(-1, -1, GameEngine.id, azione));
		inviaAzione(new Messaggio(-1, -1, GameEngine.id, azione));
	}

	@Override
	public void keyReleased(KeyEvent e) { //rilascio tasto
		int key = e.getKeyCode();
		Azione azione = null;
		
		if(key == KeyEvent.VK_SPACE) {
			spazio = false;
			timer.stop();
			if(potenza != 0) {
				azione = Messaggio.Azione.SHOOT;
	            System.out.println("potenza: "+potenza);
			}
		}
		if(key == KeyEvent.VK_UP) {
			up = false;
			azione = Azione.NUP;
		}
		if(key == KeyEvent.VK_DOWN) {
			down = false;
			azione = Azione.NDOWN;
		}
		if(key == KeyEvent.VK_LEFT) {
			left = false;
			azione = Azione.NLEFT;
		}
		if(key == KeyEvent.VK_RIGHT) {
			right = false;
			azione = Azione.NRIGHT;
		}
		
		if(engine.stato == Stato.SCREEN) {
			engine.gestioneIp(key);
		}
		
		if(engine.stato != Stato.SCREEN && engine.stato != Stato.WAIT) {
			eseguiAzione(new Messaggio(potenza, -1, GameEngine.id, azione));
			inviaAzione(new Messaggio(potenza, -1, GameEngine.id, azione));
		}
	}
	
}
