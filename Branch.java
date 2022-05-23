import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Branch<E> {
	public E info;
	public Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> move;
	public Branch<E> parent;
	public ArrayList<Branch<E>> children=new ArrayList<Branch<E>>();;
	public double score;;
	public boolean minimax;
	
	public Branch(E info) {
		this.info=info;
		minimax=true;
		move=null;
		parent=null;
		score=Double.NEGATIVE_INFINITY;
	}
	
	public Branch(E info, boolean minimax, Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> move, Branch<E> parent) {
		this.info=info;
		this.minimax=minimax;
		this.move=move;
		this.parent=parent;
		score=minimax?Double.NEGATIVE_INFINITY:Double.POSITIVE_INFINITY;
	}
	
	
	public String toString() {
		return "<"+info+", "+score+", "+(parent!=null?parent.info:"head")+", "+minimax+", "+move+">";
	}
	
	public boolean equals(Branch<E> branch) {
		return info.equals(branch.info);
	}
}
