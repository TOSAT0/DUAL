package main;

import java.util.Scanner;

import control.GameEngine;
import control.Server;

public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		System.out.print("?");
		if(in.nextInt() == 0)
			new Server();
		else
			new GameEngine();
	}
	
}
