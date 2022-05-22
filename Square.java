import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Square {
	private Boolean bool; //false for black, true for white
	public Square(Boolean bool) {
		this.bool=bool;
	}
	
	public Boolean getState() {
		return bool;
	}
	
	public void draw(Graphics g, int x, int y) {
		g.setColor(bool?Color.white:Color.black);
		g.fillOval(x+1, y+1, Othello.SQUARE_WIDTH-2, Othello.SQUARE_WIDTH-2);
	}
	
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	public boolean equals(Square b) {
		return this.getClass()==b.getClass();
	}
}
