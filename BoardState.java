import java.util.Arrays;

public class BoardState extends Board {
	private Square[][] board;
	
	public BoardState(Square[][] board) {
		super(board.length, board);
		this.board=board;
	}
}
