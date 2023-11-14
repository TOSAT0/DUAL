package main;

import java.awt.Toolkit;
import java.util.Scanner;

import control.GameEngine;
import control.Server;

public class Main {

	public static void main(String[] args) {
		System.out.println("h: "+(Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
		System.out.println("w: "+(Toolkit.getDefaultToolkit().getScreenSize().getWidth()));
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("?");
		if(in.nextInt() == 0)
			new Server();
		else
			new GameEngine();
	}
	
}
