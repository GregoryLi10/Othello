import java.util.ArrayList;

public class Node<E> {

	public E info;
	public int score;
	public Node<E> parent;
	public boolean redBlack;
	public ArrayList<Node<E>> children;
	
	public Node(E info, int score, Node<E> parent, boolean redBlack, ArrayList<Node<E>> children) {
		this.info=info;
		this.score=score;
		this.parent=parent;
		this.redBlack=redBlack;
		this.children=children;
	}
	
	public Node(E info, int score, Node<E> parent, boolean redBlack) { //end 
		this(info, score, parent, redBlack, null);
	}
	
//	public Node(E info, boolean redBlack, ArrayList<Node<E>> children) { //head
//		for (int i=0; i<children.size(); i++) {
//			child
//		}
//	}
	
	public String toString() {
		return "<"+info+", "+score+", "+(parent!=null?parent.info:"head")+", "+redBlack+">";
	}
}
