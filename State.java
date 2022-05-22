import java.util.Arrays;

public class State extends Board {
	private Square[][] board;
	
	public State(Square[][] board) {
		super(board.length, board);
		this.board=board;
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (Square[] row:board)
			sb.append(Arrays.toString(row));
		return sb.toString();
	}
}
