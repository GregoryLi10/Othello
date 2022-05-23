import java.util.ArrayList;

public class BranchTest<E> {

	public E info;
	public ArrayList<BranchTest<E>> children;
	public BranchTest<E> parent;
	
	public BranchTest(E info) {
		this(info, null);
	}
	
	public BranchTest(E info, BranchTest<E> parent) {
		this.info=info;
		this.parent=parent;
	}
	
	public String toString() {
		return parent!=null?info.toString():"<Info: "+info+" Children: "+children+">";
	}
	
	public boolean equals(BranchTest<E> b) {
		return info.equals(b.info);
	}
}
