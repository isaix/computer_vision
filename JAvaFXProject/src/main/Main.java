package main;

import carConnection.SocketRunnable;

public class Main {

	public static void main(String[] args) {
				
		Runnable runnable = new SocketRunnable();
		
		Thread thread = new Thread(runnable);
		
		thread.start();

	}

}
