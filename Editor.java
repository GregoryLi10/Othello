import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.*;

public class Editor {

	private char[] str;
	private ArrayList<String> undo=new ArrayList<String>(), redo=new ArrayList<String>();
	
	public Editor(String str) {
		this.str=str.toCharArray();
	}
	
	public Editor() {
		this.str=new char[0];
	}
	
	public String undo() {
		try {
			String temp=String.valueOf(str);
			str=undo.remove(undo.size()-1).toCharArray();
			redo.add(temp);
			return String.valueOf(str);
		} catch (Exception e) {
			return String.valueOf(str);
		}
	}
	
	public String redo() {
		try {
			String temp=String.valueOf(str);
			str=redo.remove(redo.size()-1).toCharArray();
			undo.add(temp);
			return String.valueOf(str);
		} catch (Exception e) {
			return String.valueOf(str);
		}
	}
	
	public String append(String input) {
		undo.add(String.valueOf(str));
		char[] temp= Arrays.copyOf(str, str.length + input.length());
		System.arraycopy(input.toCharArray(), 0, temp, str.length, input.length());
		str=temp;
		return String.valueOf(str);
	}
	
	public String set(String input) {
		undo.add(String.valueOf(str));
		str=input.toCharArray();
		return input;
	}
	
	public String toString() {
		return String.valueOf(str);
	}
	
	public static void main (String[] args) {
		Editor sb=new Editor("hi");
	}
}
