package carConnection;

import Algorithm.Move;

public class SocketRunnable implements Runnable{

	@Override
	public void run() {
			
		SocketClient client = new SocketClient();
		client.startConnection("192.168.43.174", 6666);
		
		boolean connected = true; 
		
		int i = 0; 
		
		Move[] moves = new Move[] {new Move(30, 90), new Move(30, 90), new Move(30, 90), new Move(30, 90)};
		
		
		while(connected) {
			//hent move-objekt
			
			
			if(i == moves.length) {
				break;
			}
			
			
			String resp = client.sendMove(moves[i]);
			
			System.out.println(resp);
			
			i++;
			
			if(resp.toLowerCase().equals("done")) {
				client.stopConnection();
				connected = false; 
			}
		}
		
	}

}
