package carConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import algorithm.Move;

public class SocketClient {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private ObjectOutputStream objectOutputStream;

	public void startConnection(String ip, int port) {
		try {
			clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e) {

		}
	}

	public String sendMessage(String msg) {
		try {
			out.println(msg);
			String resp = in.readLine();
			return resp;
		} catch(Exception e) {
			e.printStackTrace();
			return "o shit";
		}
	}
	
	public String sendMoves(ArrayList<Move> moves) {
		try {
			objectOutputStream.writeObject(moves);
			String resp = in.readLine();
			return resp;
		} catch(Exception e) {
			return "o shit";
		}
	}

	public void stopConnection() {
		try {
			in.close();
			out.close();
			clientSocket.close();
		} catch(Exception e) {

		}
	}
	
}
