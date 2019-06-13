package Algorithm;
	
import java.util.*;
	
public class Node implements Comparable{
	
	private int number;
	private int x;
	private int y;
	private HashMap<Integer, Double> distances;
	
	public Node() {
		distances = new HashMap<Integer, Double>();
	}
	public Node(int number, int x, int y) {
		distances = new HashMap<Integer, Double>();
		this.x = x;
		this.y = y;
		this.number = number;
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public HashMap<Integer, Double> getDistances() {
		return distances;
	}
	public void setDistances(HashMap<Integer, Double> distances) {
		this.distances = distances;
	}
	public void addDistance(int number, double distance) {
		distances.put(number, distance);
	}
	@Override
	public int compareTo(Object compnode) {
		return this.number - ((Node)compnode).getNumber();
	}
	
}	
