package algorithm;

public class HelperNode extends Node {
	
	Node nodePointer;

	public HelperNode(int number, int x, int y) {
		super(number, x, y);
	}
	
	public Node getNodePointer() {
		return nodePointer;
	}

	public void setNodePointer(Node nodePointer) {
		this.nodePointer = nodePointer;
	}

}
