package mitzi;

import java.util.HashMap;
import java.util.Map;

public class HashManager {

	private PositionTreeNode root_node;

	private Map<IBoard, PositionTreeNode> hash_map = new HashMap<IBoard, PositionTreeNode>();

	HashManager(Position position) {
		root_node = new PositionTreeNode(position);
		hash_map.put(position.board, root_node);
	}

	public void rebase(PositionTreeNode new_root) {
		new_root.removeFromParent();
		new_root.setParentMove(null);
		root_node = new_root;
		hash_map = new HashMap<IBoard, PositionTreeNode>();
	}

	public PositionTreeNode lookup(Position position) {
		PositionTreeNode ptn = hash_map.get(position.board);

		// check for cache hit
		if (ptn != null) {
			return ptn;
		}

		// register new PTN
		ptn = new PositionTreeNode(position);
		hash_map.put(position.board, ptn);

		return ptn;
	}

	public PositionTreeNode getRootNode() {
		return root_node;
	}

}
