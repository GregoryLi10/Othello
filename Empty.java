import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public class Empty extends Square {

	public Empty() {
		super(null);
	}
	
	@Override
	public void draw(Graphics g, int x, int y) {
		return; 
	}
}
