import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class AI {

	private HashMap<Branch<Board>, Integer> next=new HashMap<Branch<Board>, Integer>();
	private Square[][] board;
	public boolean team;
	public Branch<Board> head=null;
	
	public AI(Board board, boolean team) {
		this.board=new Square[board.getBoard().length][board.getBoard().length];
		for(int i=0; i<this.board.length; i++) 
			this.board[i]=Arrays.copyOf(board.getBoard()[i], this.board.length);
		this.team=team;
	}
	
	public Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> bestMove(Board board, boolean turn) {
		this.board=board.getBoard();
		if (team!=turn) return null;
		Branch<Board> head=new Branch<Board>(board);
		this.head=head;
		HashMap<Branch<Board>, ArrayList<Branch<Board>>> map=new HashMap<Branch<Board>, ArrayList<Branch<Board>>>();
		getTree(team, head, map, new ArrayList<Branch<Board>>());
		getHead(map);
		minimax(head, -1, true);
		double max=Double.NEGATIVE_INFINITY;
		Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> move=null;
		for (int i=0; i<head.children.size(); i++) {
			if (head.children.get(i).score>max) {
				max=head.children.get(i).score;
				move=head.children.get(i).move;
			}
		}
		return move;
	}
	
	public double minimax(Branch<Board> curr, int depth, boolean redBlack) {
		int[] temp=curr.info.score();
		if (depth==0||curr.info.gameOver()) {
			curr.score=temp[team?1:0]-temp[team?0:1];
			return curr.score;
		}
		if (redBlack) {
			double maxEval=Double.NEGATIVE_INFINITY;
			for (Branch<Board> child:curr.children) {
				curr.score=minimax(child, depth-1, false);
				maxEval=Math.max(curr.score, maxEval);
			}
			return maxEval;
		}
		else {
			double minEval=Double.POSITIVE_INFINITY;
			for (Branch<Board> child:curr.children) {
				curr.score=minimax(child, depth-1, true);
				minEval=Math.min(curr.score, minEval);
			}
			return minEval;
		}
	}
	
	public void getHead(HashMap<Branch<Board>, ArrayList<Branch<Board>>> map) {
		for (Entry<Branch<Board>, ArrayList<Branch<Board>>> entry:map.entrySet()) {
			entry.getKey().children=entry.getValue();
			
		}
	}
	
	public HashMap<Branch<Board>, ArrayList<Branch<Board>>> getTree(boolean redBlack, Branch<Board> branch, HashMap<Branch<Board>, ArrayList<Branch<Board>>> map, ArrayList<Branch<Board>> list) {
		for (Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> entry:branch.info.getMoves(redBlack).entrySet()) {
			Board temp=new BoardState(branch.info.getBoard());
			temp.place(entry.getKey().get(0), entry.getKey().get(1), redBlack, entry.getValue());
			Branch<Board> newBranch=new Branch<Board>(temp, team==redBlack, entry, branch);
			list.add(newBranch);
			getTree(!redBlack,newBranch,map,new ArrayList<Branch<Board>>());
		}
		if (!list.isEmpty())
			map.put(branch, list);
		return map;
	}
}
