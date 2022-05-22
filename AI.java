import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class AI {

	private HashMap<Node<Board>, Integer> games=new HashMap<Node<Board>, Integer>();
	private Square[][] board;
	private boolean team;
	
	public AI(Board board, boolean team) {
		this.board=new Square[board.getBoard().length][board.getBoard().length];
		for(int i=0; i<this.board.length; i++) 
			this.board[i]=Arrays.copyOf(board.getBoard()[i], this.board.length);
		this.team=team;
	}
	
//	public int[] bestMove(Board board, boolean turn) {
//		ArrayList<Node<Board>> tree=new ArrayList<Node<Board>>();
//		Node<Board> head=new Node<Board>(board, false);
//		tree=getTree(board, turn, false, tree, head);
//		for (int i=0; i<tree.size(); i++) {
//			games.put(tree.get(i), tree.get(i).score);
//		}
//		Node<Board> best=games.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
//		Node<Board> move=null, curr=best;
//		while (curr.parent.parent.parent!=null) {
//			move=curr;
//			curr=curr.parent;
//		}
//		System.out.println(move);
//		return null;
//	}
//	
//	public ArrayList<Node<Board>> getTree(Board board, boolean turn, boolean redBlack, ArrayList<Node<Board>> tree, Node<Board> node) {
//		HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> moves=(HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>>)node.info.getMoves(turn).clone();
//		for (Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> entry:moves.entrySet()) {
//			Board temp=new State(node.info.getBoard());
//			temp.place(entry.getKey().get(0), entry.getKey().get(1), turn, entry.getValue());
//			if (temp.gameOver(turn))
//				tree.add(new Node<Board>(temp, (int) (temp.score()[turn?1:0]-Math.pow(board.getBoard().length,2)/2), node, redBlack));
//			else 
//				getTree(temp, !turn, !redBlack, tree, new Node<Board>(temp, node, !redBlack));
//		}
//		return tree;
//	}

	
	
}
