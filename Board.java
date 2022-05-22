import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Board {

	private Square[][] board;
	private HashMap<String, Image> images;
	private boolean gameOver;
	private ArrayList<Square[][]> undo=new ArrayList<Square[][]>(), redo=new ArrayList<Square[][]>();
	private HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> moves=new HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>>();
	
	public Board(int size) {
		board=new Square[size][size];
		for (Square[] temp:board) 
			Arrays.fill(temp, new Empty());
		board[(board.length-1)/2][(board.length-1)/2]=new White();
		board[(board.length-1)/2+1][(board.length-1)/2+1]=new White();
		board[(board.length-1)/2][(board.length-1)/2+1]=new Black();
		board[(board.length-1)/2+1][(board.length-1)/2]=new Black();
		images=loadImages();
	}
	
	public Board(int size, Square[][] board) {
		this.board=new Square[size][size];
		for (int i=0; i<board.length; i++)
			this.board[i]=Arrays.copyOf(board[i], board.length);
		images=loadImages();
	}
	
	public void draw(Graphics g) {
		int sw = Othello.SQUARE_WIDTH, xadjust=Othello.xadjust, yadjust=Othello.yadjust;
		for (int i=0; i<board.length; i++) 
			for (int j=0; j<board[i].length; j++) { 
				g.setColor(Color.black);
				g.drawRect(i*sw+xadjust, j*sw+yadjust, sw, sw); //draws squares for grid
				g.setColor(new Color(0,100,0)); //sets checkerboard color
				g.fillRect(i*sw+1+xadjust, j*sw+1+yadjust, sw-2, sw-2); //fills squares for grid
				board[i][j].draw(g, i*sw+xadjust, j*sw+yadjust); //draws pieces
			}
		if (gameOver) return; //don't draw moves if curr is null or the game is already over and catches promotion bug
		g.setColor(new Color(255, 0, 0));
		for (ArrayList<Integer> move:moves.keySet()) //draws red dots for the possible moves of the selected piece on top of everything else
			g.fillOval(move.get(0)*sw+sw/3+xadjust, move.get(1)*sw+sw/3+yadjust, sw/3, sw/3);
	}
	
	public HashMap<String, Image> loadImages() {
		return new HashMap<String, Image>(){{
			put("White", Toolkit.getDefaultToolkit().getImage("Images/White.png").getScaledInstance(Othello.IMG_WIDTH, Othello.IMG_WIDTH, Image.SCALE_SMOOTH));
			put("Black", Toolkit.getDefaultToolkit().getImage("Images/Black.png").getScaledInstance(Othello.IMG_WIDTH, Othello.IMG_WIDTH, Image.SCALE_SMOOTH));
		}};
	}
	
	public void place(int r, int c, boolean turn, ArrayList<ArrayList<Integer>> flips) {
		Square[][] temp=new Square[board.length][];
		for (int i=0; i<board.length; i++)
			temp[i]=Arrays.copyOf(board[i], board[i].length);
		undo.add(temp);
		if (!redo.isEmpty()) redo=new ArrayList<Square[][]>();
		board[r][c]=turn?new White():new Black();
		for (int i=0; i<flips.size(); i++) {
			int[] temp1=toward(new int[] {r,c}, new int[] {flips.get(i).get(0),flips.get(i).get(1)});
			for (int x=r, y=c; !(x==flips.get(i).get(0)&&y==flips.get(i).get(1)); x+=temp1[0], y+=temp1[1]) {
				board[x][y]=turn?new White():new Black();
			}
		}
	}
	
	private int[] toward(int[] a, int[] b) {
		int[] temp=new int[2];
		temp[0]=a[0]==b[0]?0:a[0]>b[0]?-1:1;
		temp[1]=a[1]==b[1]?0:a[1]>b[1]?-1:1;
		return temp;
	}
	
	public HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> getMoves(boolean turn) { //turn and getState same color - false is black, true is white (black goes first)
		/*
		 * so just find turn==getState then branch in board.length directions to find a valid move 
		 * 
		 */
		moves=new HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>>();
//		System.out.println(turn==board[3][3].getState());
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				if (board[i][j].getState()==null||board[i][j].getState()!=turn) continue; //guard until same color dot appears
				boolean[] inval=new boolean[8];
				for (Direction dir: Direction.values()) {
					switch (dir) {
					case UP: 
//						System.out.println((turn?"white":"black")+" "+i+" "+j);
						if (j-1<0||board[i][j-1].getState()==null||board[i][j-1].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (j-c<0||inval[0]) break;
							if (board[i][j-c].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i, j-c)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i, j-c)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i, j-c))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[0]=board[i][j-c].getState()==turn;
						}
					case DOWN:
						if (j+1>=board.length||board[i][j+1].getState()==null||board[i][j+1].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (j+c>=board.length||inval[1]) break;
							if (board[i][j+c].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i, j+c)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i, j+c)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i, j+c))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[1]=board[i][j+c].getState()==turn;
						}
					case LEFT:
						if (i-1<0||board[i-1][j].getState()==null||board[i-1][j].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (i-c<0||inval[2]) break;
							if (board[i-c][j].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i-c, j)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i-c, j)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i-c, j))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[2]=board[i-c][j].getState()==turn;
						}
					case RIGHT: 
						if (i+1>=board.length||board[i+1][j].getState()==null||board[i+1][j].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (i+c>=board.length||inval[3]) break;
							if (board[i+c][j].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i+c, j)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i+c, j)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i+c, j))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[3]=board[i+c][j].getState()==turn;
						}
					case UPLEFT:
						if (i-1<0||j-1<0||board[i-1][j-1].getState()==null||board[i-1][j-1].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (i-c<0||j-c<0||inval[4]) break;
							if (board[i-c][j-c].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i-c, j-c)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i-c, j-c)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i-c, j-c))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[4]=board[i-c][j-c].getState()==turn;
						}
					case UPRIGHT: 
						if (i+1>=board.length||j-1<0||board[i+1][j-1].getState()==null||board[i+1][j-1].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (i+c>=board.length||j-c<0||inval[5]) break;
							if (board[i+c][j-c].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i+c, j-c)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i+c, j-c)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i+c, j-c))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[5]=board[i+c][j-c].getState()==turn;
						}
					case DOWNLEFT:
						if (i-1<0||j+1>=board.length||board[i-1][j+1].getState()==null||board[i-1][j+1].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (i-c<0||j+c>=board.length||inval[6]) break;
							if (board[i-c][j+c].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i-c, j+c)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i-c, j+c)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i-c, j+c))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[6]=board[i-c][j+c].getState()==turn;
						}
					case DOWNRIGHT:
						if (i+1>=board.length||j+1>=board.length||board[i+1][j+1].getState()==null||board[i+1][j+1].getState()==turn) break; 
						for (int c=2; c<board.length; c++) {
							if (i+c>=board.length||j+c>=board.length||inval[7]) break;
							if (board[i+c][j+c].getState()==null) {
								moves.put(new ArrayList<Integer>(Arrays.asList(i+c, j+c)), moves.containsKey(new ArrayList<Integer>(Arrays.asList(i+c, j+c)))?and(moves.get(new ArrayList<Integer>(Arrays.asList(i+c, j+c))),new ArrayList<Integer>(Arrays.asList(i, j))):and(new ArrayList<ArrayList<Integer>>(), new ArrayList<Integer>(Arrays.asList(i,j))));
								break;
							}
							inval[7]=board[i+c][j+c].getState()==turn;
						}
					default:
						break;
					} //switch
				} //for each dir
			} //for j
		} //for i
		return moves;
	}
	
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		UPLEFT,
		UPRIGHT,
		DOWNLEFT,
		DOWNRIGHT
	}
	
	private ArrayList<ArrayList<Integer>> and(ArrayList<ArrayList<Integer>> list, ArrayList<Integer> temp){
		list.add(temp);
		return list;
	}
	
	public Square[][] getBoard(){
		return board;
	}
	
	public Square[][] undo() throws Exception {
		Square[][] temp=new Square[board.length][];
		for (int i=0; i<board.length; i++)
			temp[i]=Arrays.copyOf(board[i], board[i].length);
		Square[][] t=undo.remove(undo.size()-1);
		for (int i=0; i<board.length; i++)
			board[i]=Arrays.copyOf(t[i], t[i].length);
		redo.add(temp);
		return board;
	}
	
	public Square[][] redo() throws Exception {
		Square[][] temp=new Square[board.length][];
		for (int i=0; i<board.length; i++) {
			temp[i]=Arrays.copyOf(board[i], board[i].length);
		}
		Square[][] t=redo.remove(redo.size()-1);
		for (int i=0; i<board.length; i++)
			board[i]=Arrays.copyOf(t[i], t[i].length);
		undo.add(temp);
		return board;
	}
	
	public boolean equals(Board b) {
		for (int i=0; i<board.length; i++) 
			for (int j=0; j<board.length; j++) 
				if (!board[i][j].equals(b.getBoard()[i][j]))
					return false;
		return true;
	}
	
	public int[] score() {
		int[] score=new int[2];
		for (Square[] row:board)
			for (Square cell:row)
				if (cell.getState()==null) continue;
				else score[cell.getState()?1:0]++;
		return score;
	}
	
	public boolean gameOver(boolean turn) {	// check if no moves for 2 moves in a row // check if no nulls
		if (getMoves(turn).size()==0&&getMoves(!turn).size()==0)
			return true;
		for (Square[] row:board)
			for (Square sqr:row)
				if (sqr.getState()==null) return false;
		return true;
	}
}
