//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Node<E> {
//
//	public E info;
//	public int score;
//	public Node<E> parent;
//	public boolean minimax;
//	public HashMap<Node<E>, Integer> children;
//	
//	public Node(E info, int score, Node<E> parent, boolean minimax, HashMap<Node<E>, Integer> children) {
//		this.info=info;
//		this.score=score;
//		this.parent=parent;
//		this.minimax=minimax;
//		this.children=children;
//	}
//	
//	public Node(E info, int score, Node<E> parent, boolean minimax) { //end 
//		this(info, score, parent, minimax, null);
//	}
//	
//	public Node(E info, ArrayList<Node<E>> children) { //head
//		this.info=info;
//		score=max(children).score;
//		parent=null;
//		minimax=true;
//	}
//	
//	public Node(E info, Node<E> parent, boolean minimax, ArrayList<Node<E>> children) { //head
//		this.info=info;
//		score=minimax?max(children).score:min(children).score;
//		this.parent=parent;
//		this.minimax=minimax;
//		this.children=new HashMap<Node<E>, Integer>();
//	}
//	
//	public Node<E> max(ArrayList<Node<E>> children) {
//		this.children=new HashMap<Node<E>,Integer>();
//		for (Node<E> node:children)
//			this.children.put(node, node.score);
//		return this.children.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
//	}
//
//	public Node<E> min(ArrayList<Node<E>> children) {
//		for (Node<E> node:children)
//			this.children.put(node, node.score);
//		return this.children.entrySet().stream().min((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
//	}
//	
//	public String toString() {
//		return "<"+info+", "+score+", "+(parent!=null?parent.info:"head")+", "+minimax+">";
//	}
//	
//	public boolean equals(Node<E> node) {
//		return info.equals(node.info);
//	}
//}
