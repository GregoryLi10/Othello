import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class TreeTest {

	public static void run(BranchTest<String> branch, HashMap<BranchTest<String>, ArrayList<BranchTest<String>>> map, ArrayList<BranchTest<String>> list) {
		for (int i=branch.info.length(); i<4; i++) {
			String temp=branch.info;
			BranchTest<String> newBranch=new BranchTest<String>(temp+i, branch);
			list.add(newBranch);
			run(newBranch, map, new ArrayList<BranchTest<String>>());
		}
		if (!list.isEmpty())
			map.put(branch, list);
		return;
	}
	
	public static void run2(HashMap<BranchTest<String>, ArrayList<BranchTest<String>>> map) {
		for (Entry<BranchTest<String>, ArrayList<BranchTest<String>>> entry:map.entrySet()) {
//			for (int i=0; i<entry.getValue().size(); i++) {
				 entry.getKey().children=entry.getValue();
//			}
		}
	}
	
	public static void main(String[] args) {
		/* 
		 * head -> [3]
		 * 
		 * 
		 */
		
		HashMap<BranchTest<String>, ArrayList<BranchTest<String>>> map=new HashMap<BranchTest<String>, ArrayList<BranchTest<String>>>();
		BranchTest<String> head=new BranchTest<String>("0");
		run(head,map,new ArrayList<BranchTest<String>>());
		run2(map);
		System.out.println(head);
		System.out.println(head.children.get(2));
	}
	
}
