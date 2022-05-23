import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Collections;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.*;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Othello {
	public static final int boardSquares=4;
	public static int width = 600;
	public static int SQUARE_WIDTH = width/boardSquares;
	public static int IMG_WIDTH = SQUARE_WIDTH;
	public static int xadjust=0, yadjust=0; 
	public static boolean splashScreen=true;

	private Board board;

	private int lastx = 0,lasty = 0;

	private boolean turn = false; //false=black, true=white;

	private boolean game_over = false, gameOngoing=false, noMoves=false, mode=false; //false=2 player, true=single player

	private JFrame frame;
	
	private int[] button=new int[] {width/4, width*5/7, width/4, width/8};
	
	private AI ai;
	
	private Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> aiMove=null;
	
	private Thread thread=new Thread(){
		@Override
		public void run() {
			Board temp=board;
			while(true) {
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.println(temp+"\n");
				if (!board.equals(temp)) {
					Long time=System.currentTimeMillis();
					temp=new BoardState(board.getBoard());
					aiMove=ai.bestMove(board, turn);
					System.out.println(aiMove);
					if (mode&&turn==ai.team) {
						while (System.currentTimeMillis()-time<500) {
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						board.place(aiMove.getKey().get(0), aiMove.getKey().get(1), turn, aiMove.getValue());
						turn=!turn;
						board.getMoves(turn);
						frame.getContentPane().repaint();
					}
				}
			}
		}
	};
	
	public static void main(String[] args) {
		new Othello(); 
	}
	
	public Othello() {
		board = new Board(boardSquares);
		frame = new JFrame();
		frame.setSize(width+2, width+24);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board.getMoves(turn);
		ai=new AI(board, true);
		thread.start();
		JPanel canvas = new JPanel() {
			public void paint(Graphics g) {
				width = frame.getWidth()<frame.getHeight()?frame.getWidth():frame.getHeight()-24; //sets new width when resized
				SQUARE_WIDTH = width/boardSquares; IMG_WIDTH = SQUARE_WIDTH; //sets all variables to follow
				xadjust=Math.abs(frame.getWidth()-width)/2; yadjust=Math.abs(frame.getHeight()-width-24)/2; //gets adjustment value
				button=new int[] {width/4, width*5/7, width/4, width/8};
				if (splashScreen) {
					g.setColor(Color.black);
					g.fillRect(xadjust, yadjust, width, width);
					g.setColor(Color.red);
					g.fillRoundRect(button[0]+xadjust-button[2]/2, button[1]+yadjust, button[2], button[3], 5, 5);
					g.fillRoundRect(3*button[0]+xadjust-button[2]/2, button[1]+yadjust, button[2], button[3], 5, 5);
					g.setFont(new Font("Calibri", Font.BOLD, width/15));
					g.drawString("Othello/Reversi", width/2-g.getFontMetrics().stringWidth("Othello/Reversi")/2+xadjust, width/4+yadjust);
					g.setFont(new Font("Calibri", Font.BOLD, width/25));
					if (game_over) {
						g.drawString(board.score()[0]+"-"+board.score()[1], width/2-g.getFontMetrics().stringWidth(board.score()[0]+"-"+board.score()[1])/2+xadjust, width/9+yadjust);
						g.drawString(board.score()[0]==board.score()[1]?"Draw!":board.score()[0]>board.score()[1]?"Black wins!":"White wins!", width/2-g.getFontMetrics().stringWidth(board.score()[0]==board.score()[1]?"Draw!":board.score()[0]>board.score()[1]?"Black wins!":"White wins!")/2+xadjust, width/6+yadjust);
						g.drawString("Press esc to review game", width/2-g.getFontMetrics().stringWidth("Press esc to review game")/2+xadjust, width/3+yadjust);
					} else g.drawString("Press esc to re-open this splashscreen", width/2-g.getFontMetrics().stringWidth("Press esc to re-open this splashscreen")/2+xadjust, width/3+yadjust);
					g.setColor(Color.black);
					g.drawString("1 Player", button[0]-g.getFontMetrics().stringWidth("1 Player")/2+xadjust, button[1]+button[3]/2+width/60+yadjust);
					g.drawString("2 Player", 3*button[0]-g.getFontMetrics().stringWidth("2 Player")/2+xadjust, button[1]+button[3]/2+width/60+yadjust);
				}
				else {
					board.draw(g);
				}
			}
		};
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (splashScreen) {
					int x=e.getX(), y=e.getY();
					if (x>=button[0]+xadjust-button[2]/2&&x<=button[0]+xadjust-button[2]/2+button[2]&&y>=button[1]+yadjust&&y<=button[1]+yadjust+button[3]) {
						mode=true;
						splashScreen=false;
						gameOngoing=true;
						turn=false;
						game_over=false;
						board = new Board(boardSquares);
						board.getMoves(turn);
					} else if (x>=3*button[0]+xadjust-button[2]/2&&x<=3*button[0]+xadjust-button[2]/2+button[2]&&y>=button[1]+yadjust&&y<=button[1]+yadjust+button[3]) {
						splashScreen=false;
						gameOngoing=true;
						turn=false;
						game_over=false;
						mode=false;
						board = new Board(boardSquares);
						board.getMoves(turn);
					}
						
				}
				else
				if (!game_over) {
					if (mode) {
						HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> moves=(HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>>)board.getMoves(turn).clone();
						if (turn!=ai.team) {
							if (moves.isEmpty()) {
								noMoves=true;
								JOptionPane.showMessageDialog(null, "You have no moves!");
								turn=!turn;
								board.getMoves(turn);
								frame.getContentPane().repaint();
								return;
							}
							int x=(e.getX()-xadjust)/SQUARE_WIDTH, y=(e.getY()-yadjust)/SQUARE_WIDTH;
							if (y>=boardSquares||y<0||x>=boardSquares||x<0) return;
							Square clicked=board.getBoard()[x][y];
							if (clicked.getState()==null) {
								Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> temp=null;
								for (Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> entry:moves.entrySet()) 
									if (entry.getKey().equals(new ArrayList<Integer>(Arrays.asList(x,y)))) { 
										temp=entry;
										break;
									}
								if (temp==null) return;
								noMoves=false;
								board.place(x, y, turn, temp.getValue());
								turn=!turn;
								board.getMoves(turn);
							}
						}
					}
					else {
					HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> moves=(HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>>)board.getMoves(turn).clone();
					if (moves.isEmpty()) {
						noMoves=true;
						JOptionPane.showMessageDialog(null, "You have no moves!");
						turn=!turn;
						board.getMoves(turn);
						frame.getContentPane().repaint();
						return;
					}
					int x=(e.getX()-xadjust)/SQUARE_WIDTH, y=(e.getY()-yadjust)/SQUARE_WIDTH;
					if (y>=boardSquares||y<0||x>=boardSquares||x<0) return;
					Square clicked=board.getBoard()[x][y];
					if (clicked.getState()==null) {
						Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> temp=null;
						for (Entry<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> entry:moves.entrySet()) 
							if (entry.getKey().equals(new ArrayList<Integer>(Arrays.asList(x,y)))) { 
								temp=entry;
								break;
							}
						if (temp==null) return;
						noMoves=false;
						board.place(x, y, turn, temp.getValue());
						turn=!turn;
						board.getMoves(turn);
//						System.out.println(ai.bestMove(board, turn));
					}
					}
				}
				frame.getContentPane().repaint();
				if (board.gameOver()) {
					game_over=true;
					splashScreen=true;
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if(!splashScreen) {
			    if (key == KeyEvent.VK_LEFT) {
			    	try {
				    	board.undo();
				    	turn=!turn;
				    	board.getMoves(turn);
				    	frame.getContentPane().repaint();
			    	} catch (Exception e1) {}
			    }

			    if (key == KeyEvent.VK_RIGHT) {
			    	try {
			    		board.redo();
			    		turn=!turn;
			    		board.getMoves(turn);
			    		frame.getContentPane().repaint();
			    	} catch (Exception e1) {}
			    }

			    if (key == KeyEvent.VK_UP) {
			    	try {
			    		while (true) {
			    			board.undo();
			    			turn=!turn;
			    			board.getMoves(turn);
			    		}
			    	} catch (Exception e1) {
			    		frame.getContentPane().repaint();
			    	}
			    }

			    if (key == KeyEvent.VK_DOWN) {
			    	try {
			    		while (true) {
			    			board.redo();
			    			turn=!turn;
			    			board.getMoves(turn);
			    		}
			    	} catch (Exception e1) {
			    		frame.getContentPane().repaint();
			    	}
			    }
				}
			    
			    if (key == KeyEvent.VK_ESCAPE&&gameOngoing) {
			    	splashScreen=!splashScreen;
			    	frame.getContentPane().repaint();
			    }
			}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		canvas.setFocusable(true);
		
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
		try {
			Thread.sleep(250);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame.getContentPane().repaint();
	}
}
